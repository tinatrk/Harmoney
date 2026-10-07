package com.example.harmoney.data.exchangeRate

import androidx.sqlite.SQLiteException
import com.example.harmoney.core.util.Resource
import com.example.harmoney.data.exchangeRate.db.converter.ExchangeRateDBConverter
import com.example.harmoney.data.exchangeRate.db.dao.ExchangeRateDao
import com.example.harmoney.data.exchangeRate.db.entity.ExchangeRateEntity
import com.example.harmoney.data.exchangeRate.db.entity.ExchangeRateSyncEntity
import com.example.harmoney.data.exchangeRate.netwok.converter.ExchangeRateDtoConverter
import com.example.harmoney.data.network.NetworkResult
import com.example.harmoney.data.network.RemoteDataSource
import com.example.harmoney.domain.exchangeRate.api.repository.ExchangeRateRepository
import com.example.harmoney.domain.exchangeRate.models.ExchangeRateFailure
import com.example.harmoney.domain.models.Currency
import com.example.harmoney.domain.models.ExchangeRate
import com.example.harmoney.domain.models.ExchangeRateResult
import timber.log.Timber
import java.math.BigDecimal
import java.time.LocalDate

class ExchangeRateRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val exchangeRateDao: ExchangeRateDao,
    private val exchangeRateDBConverter: ExchangeRateDBConverter,
    private val exchangeRateDtoConverter: ExchangeRateDtoConverter,
) : ExchangeRateRepository {

    private val baseCurrency = Currency.EUR

    override suspend fun getRates(
        sources: Set<Currency>,
        target: Currency,
    ): Resource<List<ExchangeRateResult>, ExchangeRateFailure> {

        if (sources.isEmpty()) return Resource.Success(emptyList())

        return try {
            val result = getAvailableRatesFromBaseToQuote(
                baseCurrency, sources + target
            )
            val ratesFromBaseToAll = exchangeRateDBConverter.map(result)

            val rates = sources.map { source ->
                when {
                    // это условие идет строго первым, иначе некорректно обработается случай
                    // source.id = target.id = baseCurrency.id -> rate not required
                    source.id == target.id -> {
                        ExchangeRateResult.NotRequired(source, target)
                    }

                    source.id == baseCurrency.id -> {
                        getBaseToTargetRate(target, ratesFromBaseToAll)
                    }

                    target.id == baseCurrency.id -> {
                        getSourceToBaseRate(source, ratesFromBaseToAll)
                    }

                    else -> {
                        getCrossRate(source, target, ratesFromBaseToAll)
                    }
                }
            }

            Resource.Success(rates)
        } catch (e: SQLiteException) {
            Timber.e(e, "Error getting exchange rates")
            Resource.Error(ExchangeRateFailure.DatabaseError)
        }
    }

    /** Обязательное условие `source` == `baseCurrency` && `target` != `baseCurrency`
     *
     * В противном случае будет возвращенно некорректное значение для случая
     * `source` == `baseCurrency` && `target` == `baseCurrency`
     * (вернется NotAvailable вместо NotRequired)
     */
    private fun getBaseToTargetRate(
        target: Currency,
        ratesFromBaseToAll: List<ExchangeRate>
    ): ExchangeRateResult {
        require(target.id != baseCurrency.id) {
            "Target currency must differ from base currency"
        }

        val fromBaseToTarget = getRateFromBaseToCurrency(currency = target, ratesFromBaseToAll)
            ?: return ExchangeRateResult.NotAvailable(baseCurrency, target)

        return ExchangeRateResult.Available(
            ExchangeRate(
                sourceCurrency = baseCurrency,
                targetCurrency = target,
                rate = fromBaseToTarget.rate,
                rateDate = fromBaseToTarget.rateDate,
            )
        )
    }

    /** Обязательное условие  `source` != `baseCurrency` && `target` == `baseCurrency`
     *
     * В противном случае будет возвращенно некорректное значение для случая
     * `source` == `baseCurrency` && `target` == `baseCurrency`
     *  (вернется NotAvailable вместо NotRequired)
     */
    private fun getSourceToBaseRate(
        source: Currency,
        ratesFromBaseToAll: List<ExchangeRate>
    ): ExchangeRateResult {
        require(source.id != baseCurrency.id) {
            "Source currency must differ from base currency"
        }

        val fromBaseToSource = getRateFromBaseToCurrency(currency = source, ratesFromBaseToAll)
            ?: return ExchangeRateResult.NotAvailable(source, baseCurrency)

        return ExchangeRateResult.Available(
            ExchangeRate(
                sourceCurrency = source,
                targetCurrency = baseCurrency,
                rate = BigDecimal.ONE.divide(fromBaseToSource.rate),
                rateDate = fromBaseToSource.rateDate,
            )
        )
    }

    /** Обязательное условие `source` != `target` && `source` != `baseCurrency` &&
     * `target` != `baseCurrency`
     *
     * В противном случае некорректные значения будут возвращены для особых ситуаций:
     *
     * `source` = `target`,
     *
     * `source` = `baseCurrency`,
     *
     * `target` = `baseCurrency`,
     *
     * `source` = `target` = `baseCurrency`
     */
    private fun getCrossRate(
        source: Currency,
        target: Currency,
        ratesFromBaseToAll: List<ExchangeRate>,
    ): ExchangeRateResult {
        require(
            source.id != target.id &&
                    source.id != baseCurrency.id &&
                    target.id != baseCurrency.id
        ) {
            "Incorrect input data for getting cross rate"
        }

        val fromBaseToSource = getRateFromBaseToCurrency(currency = source, ratesFromBaseToAll)
        val fromBaseToTarget = getRateFromBaseToCurrency(currency = target, ratesFromBaseToAll)

        if (fromBaseToSource == null || fromBaseToTarget == null) {
            return ExchangeRateResult.NotAvailable(source, target)
        }

        return ExchangeRateResult.Available(
            ExchangeRate(
                sourceCurrency = fromBaseToSource.targetCurrency,
                targetCurrency = fromBaseToTarget.targetCurrency,
                rate = fromBaseToTarget.rate.divide(fromBaseToSource.rate),
                rateDate = minOf(fromBaseToSource.rateDate, fromBaseToTarget.rateDate)
            )
        )
    }

    private fun getRateFromBaseToCurrency(
        currency: Currency,
        ratesFromBaseToAll: List<ExchangeRate>
    ): ExchangeRate? {
        return ratesFromBaseToAll.find { it.targetCurrency.id == currency.id }
    }

    private suspend fun getAvailableRatesFromBaseToQuote(
        baseCurrency: Currency,
        quoteCurrencies: Set<Currency>,
    ): List<ExchangeRateEntity> {
        val today = LocalDate.now()
        val todayEpochDay = today.toEpochDay()
        val quoteCurrencyIds = quoteCurrencies.map { it.id }

        // 1. Ищем сегодняшний курс в кеше
        val cachedRates = exchangeRateDao.getRatesByDate(
            baseCurrencyId = baseCurrency.id,
            quoteCurrencyIds = quoteCurrencyIds,
            dateEpochDay = todayEpochDay
        )

        val cachedRateIds = cachedRates
            .associateByTo(mutableMapOf()) { it.quoteCurrencyId }

        // 2. Определяем валюты, для которых нет сегодняшнего курса в кеше
        val missingQuotesInCache = quoteCurrencies
            .filterNot { it.id in cachedRateIds }

        if (missingQuotesInCache.isEmpty()) {
            return cachedRateIds.values.toList()
        }

        // 3. Для отсутствующих курсов запрашиваем актуальные данные в api и сохраняем в БД
        updateMissingRates(
            baseCurrency = baseCurrency,
            missingQuoteCurrencies = missingQuotesInCache.toSet(),
            today = today
        )

        // 4. Возвращаем обновленные курсы из кеша
        return exchangeRateDao.getRatesClosestToDate(
            baseCurrencyId = baseCurrency.id,
            quoteCurrencyIds = quoteCurrencyIds,
            dateEpochDay = todayEpochDay
        )
    }

    private suspend fun updateMissingRates(
        baseCurrency: Currency,
        missingQuoteCurrencies: Set<Currency>,
        today: LocalDate,
    ) {
        val syncedIds = exchangeRateDao.getSyncedCurrencyIds(
            baseCurrencyId = baseCurrency.id,
            quoteCurrencyIds = missingQuoteCurrencies.map { it.id },
            dateEpochDay = today.toEpochDay()
        ).toSet()

        val quotesToFetch = missingQuoteCurrencies
            .filterNot { it.id in syncedIds }
            .toSet()

        if (quotesToFetch.isNotEmpty()) {
            updateCacheRatesFromRemote(
                baseCurrency = baseCurrency,
                quoteCurrencies = quotesToFetch,
                dateFrom = today.minusDays(AMOUNT_OF_DAYS_FOR_STALE_EXCHANGE_RATE),
                dateTo = today,
            )
        }
    }

    private suspend fun updateCacheRatesFromRemote(
        baseCurrency: Currency,
        quoteCurrencies: Set<Currency>,
        dateFrom: LocalDate,
        dateTo: LocalDate,
    ) {

        val result = remoteDataSource.getRates(
            baseIsoCode = baseCurrency.isoCode,
            quoteIsoCodes = quoteCurrencies.joinToString(",") { it.isoCode },
            from = dateFrom.toString(),
            to = dateTo.toString()
        )

        if (result is NetworkResult.Success) {
            val rates = exchangeRateDtoConverter.map(result.data)

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
        private const val AMOUNT_OF_DAYS_FOR_STALE_EXCHANGE_RATE = 7L
    }
}
