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
import timber.log.Timber
import java.time.LocalDate

class ExchangeRateRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val exchangeRateDao: ExchangeRateDao,
    private val exchangeRateDBConverter: ExchangeRateDBConverter,
    private val exchangeRateDtoConverter: ExchangeRateDtoConverter,
) : ExchangeRateRepository {

    override suspend fun getRates(
        baseCurrency: Currency,
        quoteCurrencies: Set<Currency>,
    ): Resource<List<ExchangeRate>, ExchangeRateFailure> {

        if (quoteCurrencies.isEmpty()) return Resource.Success(emptyList())

        return try {
            val result = getRatesInternal(baseCurrency, quoteCurrencies)
            Resource.Success(exchangeRateDBConverter.map(result))
        } catch (e: SQLiteException) {
            Timber.e(e, "Error getting exchange rates")
            Resource.Error(ExchangeRateFailure.DatabaseError)
        }

    }

    private suspend fun getRatesInternal(
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
                dateFrom = today.minusDays(7),
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

    /*override suspend fun getRates(
        baseCurrency: Currency,
        quoteCurrency: Currency
    ): Resource<ExchangeRate, ExchangeRateFailure> {
        val response = remoteDataSource.getRate(
            baseIsoCode = baseCurrency.isoCode,
            quoteIsoCode = quoteCurrency.isoCode,
        )

        return when (response) {
            is NetworkResult.Success -> {
                if (response.data.baseIsoCode
                        .equals(baseCurrency.isoCode, ignoreCase = true)
                    && response.data.quoteIsoCode.equals(
                        quoteCurrency.isoCode,
                        ignoreCase = true
                    )
                ) {
                    val exchangeRate = ExchangeRate(
                        baseCurrency = baseCurrency,
                        quoteCurrency = quoteCurrency,
                        rate = response.data.rate,
                        rateDate = response.data.date
                            .let(LocalDate::parse),
                    )
                    Resource.Success(exchangeRate)
                } else {
                    Resource.Error(ExchangeRateFailure.NetworkError)
                }
            }

            else -> Resource.Error(ExchangeRateFailure.NetworkError)//????????

            /* -> {
                Resource.Error(ExchangeRateFailure.BadRequest)
            }

            HTTP_INTERNAL_ERROR -> {
                Resource.Error(ExchangeRateFailure.NetworkError)
            }

            HTTP_UNAVAILABLE -> {
                Resource.Error(ExchangeRateFailure.NetworkConnectionError)
            }

            else -> {
                Resource.Error(ExchangeRateFailure.RateNotAvailable)//??????????
            }*/
        }
    }*/

    /* override suspend fun getHistoricalRate(
         baseCurrency: Currency,
         quoteCurrency: Currency,
         date: LocalDate
     ): Resource<ExchangeRate, ExchangeRateFailure> {
         if (baseCurrency == quoteCurrency) {
             return Resource.Success(
                 ExchangeRate(
                     baseCurrency = baseCurrency,
                     quoteCurrency = quoteCurrency,
                     rate = BigDecimal.ONE,
                     rateDate = date,
                 )
             )
         }

         val directCachedRate = exchangeRateDao.getRate(
             baseCurrencyId = baseCurrency.id,
             quoteCurrencyId = quoteCurrency.id,
             dateEpochDay = dateConverter.dateToMillis(date)
         )

         if (directCachedRate != null) {
             return Resource.Success(
                 exchangeRateDBConverter.map(directCachedRate)
             )
         }

         val inverseCachedRate = exchangeRateDao.getRate(
             baseCurrencyId = quoteCurrency.id,
             quoteCurrencyId = baseCurrency.id,
             dateEpochDay = dateConverter.dateToMillis(date)
         )

         if (inverseCachedRate != null) {
             return Resource.Success(
                 ExchangeRate(
                     baseCurrency = baseCurrency,
                     quoteCurrency = quoteCurrency,
                     rate = BigDecimal.ONE.divide(
                         inverseCachedRate.rateToBase,
                         16,
                         RoundingMode.HALF_EVEN,
                     ),
                     rateDate = date,
                     requestedDate = date
                 )
             )
         }

         return fetchAndCache(
             baseCurrency = baseCurrency,
             quoteCurrency = quoteCurrency,
             date = date
         )
     }

     private suspend fun fetchAndCache(
         baseCurrency: Currency,
         quoteCurrency: Currency,
         date: LocalDate,
     ): Resource<ExchangeRate, ExchangeRateFailure> {

         val response = remoteDataSource.getRate(
             baseIsoCode = baseCurrency.isoCode,
             quoteIsoCode = quoteCurrency.isoCode,
             date = date.toString()
         )

         return when (response) {
             is NetworkResult.Success -> {
                 if (response.data.baseIsoCode
                         .equals(baseCurrency.isoCode, ignoreCase = true)
                     && response.data.quoteIsoCode.equals(
                         quoteCurrency.isoCode,
                         ignoreCase = true
                     )
                 ) {
                     val exchangeRateEntity = ExchangeRateEntity(
                         baseCurrencyId = baseCurrency.id,
                         quoteCurrencyId = quoteCurrency.id,
                         dateEpochDay = response.data.date.let(LocalDate::parse)
                             ?.let(LocalDate::toEpochDay) ?: -1L,//????????
                         rateToBase = response.data.rate
                     )
                     exchangeRateDao.insertRate(exchangeRateEntity)
                     Resource.Success(exchangeRateDBConverter.map(exchangeRateEntity))
                 } else {
                     Resource.Error(ExchangeRateFailure.NetworkError)
                 }
             }

             else -> Resource.Error(ExchangeRateFailure.NetworkError)//??????

             /*HTTP_BAD_REQUEST -> {
                 Resource.Error(ExchangeRateFailure.BadRequest)
             }

             HTTP_INTERNAL_ERROR -> {
                 Resource.Error(ExchangeRateFailure.NetworkError)
             }

             HTTP_UNAVAILABLE -> {
                 Resource.Error(ExchangeRateFailure.NetworkConnectionError)
             }

             else -> {
                 Resource.Error(ExchangeRateFailure.RateNotAvailable)//??????????
             }*/
         }
     }*/

    /*override suspend fun preloadRates(
        baseCurrency: Currency,
        quoteCurrencies: Set<Currency>,
        from: LocalDate,
        to: LocalDate
    ) {
        val quotesString = quoteCurrencies.filter { it.id != baseCurrency.id }
            .joinToString(",") { it.isoCode }

        if (quotesString.isEmpty()) return

        val response = remoteDataSource.getRates(
            baseIsoCode = baseCurrency.isoCode,
            quoteIsoCodes = quotesString,
            from = from.toString(),
            to = to.toString()
        )

        /*when (response.resultCode) {
            HTTP_OK -> {
                with(response as CurrencyRateListResponse) {

                    exchangeRateDao.insertAll(response.rateList.map { dto ->
                        ExchangeRateEntity(
                            baseCurrencyId = dto.baseIsoCode,//????????
                            quoteCurrencyId = dto.quoteIsoCode,
                            dateMillis = dateConverter.dateToMillis(LocalDate.parse(dto.date)),
                            rate = dto.rate
                        )
                    })
                }
            }

            /*HTTP_BAD_REQUEST -> {
                Resource.Error(ExchangeRateFailure.BadRequest)
            }

            HTTP_INTERNAL_ERROR -> {
                Resource.Error(ExchangeRateFailure.NetworkError)
            }

            HTTP_UNAVAILABLE -> {
                Resource.Error(ExchangeRateFailure.NetworkConnectionError)
            }*/

            else -> {
                //Resource.Error(ExchangeRateFailure.RateNotAvailable)//??????????
            }
        }*/
    }*/
}
