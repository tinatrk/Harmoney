package com.example.harmoney.data.exchangeRate

import androidx.sqlite.SQLiteException
import com.example.harmoney.core.util.Resource
import com.example.harmoney.data.currencyList.network.SupportedCurrenciesDataSource
import com.example.harmoney.data.currencyList.network.SupportedCurrenciesResult
import com.example.harmoney.data.exchangeRate.db.converter.ExchangeRateDBConverter
import com.example.harmoney.data.exchangeRate.db.dao.ExchangeRateDao
import com.example.harmoney.data.exchangeRate.db.entity.ExchangeRateSyncEntity
import com.example.harmoney.data.exchangeRate.netwok.converter.ExchangeRateDtoConverter
import com.example.harmoney.data.network.NetworkResult
import com.example.harmoney.data.network.RemoteDataSource
import com.example.harmoney.domain.exchangeRate.models.ExchangeRateFailure
import com.example.harmoney.domain.models.Currency
import com.example.harmoney.domain.models.ExchangeRate
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import timber.log.Timber
import java.time.Clock
import java.time.LocalDate

class ExchangeRateProviderImpl(
    private val remoteDataSource: RemoteDataSource,
    private val exchangeRateDao: ExchangeRateDao,
    private val exchangeRateDBConverter: ExchangeRateDBConverter,
    private val exchangeRateDtoConverter: ExchangeRateDtoConverter,
    private val supportedCurrenciesDataSource: SupportedCurrenciesDataSource,
    private val clock: Clock = Clock.systemDefaultZone(),
) : ExchangeRateProvider {

    private val ratesCacheMutex = Mutex()

    override suspend fun getAvailableRatesFromBaseToQuote(
        baseCurrency: Currency,
        quoteCurrencies: Set<Currency>
    ): Resource<List<ExchangeRate>, ExchangeRateFailure> {
        return try {
            if (quoteCurrencies.isEmpty()) {
                Resource.Success(emptyList())
            } else {
                getAvailableRates(baseCurrency, quoteCurrencies)
            }
        } catch (e: SQLiteException) {
            Timber.e(e, "Error getting exchange rates")
            Resource.Error(ExchangeRateFailure.DatabaseError)
        }
    }

    private suspend fun getAvailableRates(
        baseCurrency: Currency,
    quoteCurrencies: Set<Currency>
    ): Resource<List<ExchangeRate>, ExchangeRateFailure> {
        val today = LocalDate.now(clock)
        val todayEpochDay = today.toEpochDay()
        val quoteCurrencyIds = quoteCurrencies.map { it.id }

        // 1. Ищем сегодняшние курсы в кеше
        val cachedRates = exchangeRateDao.getRatesByDate(
            baseCurrencyId = baseCurrency.id,
            quoteCurrencyIds = quoteCurrencyIds,
            dateEpochDay = todayEpochDay
        )

        val cachedRateIds = cachedRates
            .associateByTo(mutableMapOf()) { it.quoteCurrencyId }

        // 2. Определяем валюты, для которых нет сегодняшнего курса в кеше
        val missingQuotesInCache = quoteCurrencies.filterNot { it.id in cachedRateIds }

        // Если все курсы актуальные, то возвращаем их
        if (missingQuotesInCache.isEmpty()) {
            return Resource.Success(
                exchangeRateDBConverter
                    .map(cachedRateIds.values.toList())
            )
        }

        // 3. Для отсутствующих курсов выделяем те, для которых сегодня еще не пытались обновить
        // курс
        val syncedIds = exchangeRateDao.getSyncedCurrencyIds(
            baseCurrencyId = baseCurrency.id,
            quoteCurrencyIds = missingQuotesInCache.map { it.id },
            dateEpochDay = todayEpochDay
        ).toSet()

        val quotesToUpdate = missingQuotesInCache.filter { it.id !in syncedIds }

        // Если среди отсутствующих курсов есть те, для которых еще не пытались обновиться сегодня,
        // то обновляем курсы

        if (quotesToUpdate.isNotEmpty()) {
            updateRatesWithLock(baseCurrency, quoteCurrencies, today)
        }

        // возвращаем самые свежие курсы из БД
        return Resource.Success(
            exchangeRateDBConverter.map(
                exchangeRateDao.getRatesClosestToDate(
                    baseCurrencyId = baseCurrency.id,
                    quoteCurrencyIds = quoteCurrencyIds,
                    dateEpochDay = todayEpochDay
                )
            )
        )
    }

    private suspend fun updateRatesWithLock(
        baseCurrency: Currency,
        quoteCurrencies: Set<Currency>,
        today: LocalDate,
    ) {
        ratesCacheMutex.withLock {
            // cнова проверяем наличие свежих курсов в БД
            // (пока ждали Mutex, данные могли обновиться)
            val currentRates = exchangeRateDao.getRatesByDate(
                baseCurrencyId = baseCurrency.id,
                quoteCurrencyIds = quoteCurrencies.map { it.id },
                dateEpochDay = today.toEpochDay()
            )

            val currentIds = currentRates
                .mapTo(mutableSetOf()) { it.quoteCurrencyId }


            // определяем курсы, для которых нет актуального значения даже после ожидания
            val stillMissingQuotes =
                quoteCurrencies.filterTo(mutableSetOf()) { it.id !in currentIds }

            // если после ожидания еще остались неактуальные курсы, то пытаемся обновиться
            if (stillMissingQuotes.isNotEmpty()) {
                updateMissingRates(
                    baseCurrency = baseCurrency,
                    missingQuoteCurrencies = stillMissingQuotes,
                    today = today
                )
            }
        }
    }

    private suspend fun updateMissingRates(
        baseCurrency: Currency,
        missingQuoteCurrencies: Set<Currency>,
        today: LocalDate,
    ) {
        if (missingQuoteCurrencies.isEmpty()) return

        val syncedIds = exchangeRateDao.getSyncedCurrencyIds(
            baseCurrencyId = baseCurrency.id,
            quoteCurrencyIds = missingQuoteCurrencies.map { it.id },
            dateEpochDay = today.toEpochDay()
        ).toSet()

        // Определяем курсы, для которых еще не пытались обновляться сегодня
        val quotesToCheck = missingQuoteCurrencies.filterTo(mutableSetOf()) {
            it.id !in syncedIds
        }

        if (quotesToCheck.isEmpty()) return

        // если среди требуемых курсов есть те, которые сегодня еще не обновлялись, запрашиваем
        // кешированный список курсов, поддержваемых api
        when (val result = supportedCurrenciesDataSource.getCurrencies()) {
            is SupportedCurrenciesResult.Available -> {
                val supportedCodes =
                    result.currencies.mapTo(mutableSetOf()) { it.isoCode.uppercase() }

                // среди требуемых курсов определяем поддерживаемые
                val supportedQuotes = quotesToCheck.filterTo(mutableSetOf()) {
                    it.isoCode.uppercase() in supportedCodes
                }

                // среди требуемых курсов определяем неподдерживаемые
                val unsupportedQuotes = quotesToCheck - supportedQuotes

                // Если список неподдерживаемых курсов не пустой И кешированный список
                // поддерживаемых валют не устарел, то помечаем, что неподдерживаемые курсы сегодня
                // уже запрашивали
                if (!result.isStale && unsupportedQuotes.isNotEmpty()) {
                    exchangeRateDao.markAsSynced(
                        unsupportedQuotes.map {
                            ExchangeRateSyncEntity(
                                baseCurrencyId = baseCurrency.id,
                                quoteCurrencyId = it.id,
                                dateEpochDay = today.toEpochDay()
                            )
                        }
                    )
                }

                // пытаемся обновить курсы, которые поддерживаются api
                if (supportedQuotes.isNotEmpty()) {
                    updateCacheRatesFromRemote(
                        baseCurrency = baseCurrency,
                        quoteCurrencies = supportedQuotes,
                        dateFrom = today
                            .minusDays(AMOUNT_OF_DAYS_FOR_STALE_EXCHANGE_RATE),
                        dateTo = today
                    )
                }
            }

            // если не смогли получить список валют, поддерживаемых api, то не обновляемся
            is SupportedCurrenciesResult.Unavailable -> {}
        }
    }

    private suspend fun updateCacheRatesFromRemote(
        baseCurrency: Currency,
        quoteCurrencies: Set<Currency>,
        dateFrom: LocalDate,
        dateTo: LocalDate,
    ) {
        if (quoteCurrencies.isEmpty()) return

        val result = remoteDataSource.getRates(
            baseIsoCode = baseCurrency.isoCode,
            quoteIsoCodes = quoteCurrencies.joinToString(",") { it.isoCode },
            from = dateFrom.toString(),
            to = dateTo.toString()
        )

        if (result is NetworkResult.Success) {
            val rates = exchangeRateDtoConverter.map(result.data)

            // Выделяем самые свежие курсы
            val latestRates = rates
                .groupBy { it.quoteCurrencyId }
                .mapNotNull { (_, ratesByCurrency) ->
                    ratesByCurrency.maxByOrNull { it.dateEpochDay }
                }

            if (latestRates.isNotEmpty()) {
                exchangeRateDao.insertRates(latestRates)
            }

            exchangeRateDao.markAsSynced(
                quoteCurrencies.map { quoteCurrency ->
                    ExchangeRateSyncEntity(
                        baseCurrencyId = baseCurrency.id,
                        quoteCurrencyId = quoteCurrency.id,
                        dateEpochDay = dateTo.toEpochDay(),
                    )
                }
            )
        }
    }

    companion object {
        private const val AMOUNT_OF_DAYS_FOR_STALE_EXCHANGE_RATE = 6L
    }
}
