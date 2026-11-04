package com.example.harmoney.data.currencyList.network

import androidx.room.withTransaction
import androidx.sqlite.SQLiteException
import com.example.harmoney.core.database.AppDatabase
import com.example.harmoney.data.currencyList.db.converter.CurrencyListDBConverter
import com.example.harmoney.data.currencyList.db.dao.CurrencyListDao
import com.example.harmoney.data.currencyList.db.entity.CurrencyListSyncEntity
import com.example.harmoney.data.currencyList.network.converter.CurrencyListDtoConverter
import com.example.harmoney.data.currencyList.network.dto.CurrencyDto
import com.example.harmoney.data.network.NetworkResult
import com.example.harmoney.data.network.RemoteDataSource
import com.example.harmoney.domain.currencyList.models.CurrencyListFailure
import com.example.harmoney.domain.models.Currency
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import timber.log.Timber
import java.time.LocalDate

class SupportedCurrenciesDataSourceImpl(
    private val database: AppDatabase,
    private val currencyListDao: CurrencyListDao,
    private val remoteDataSource: RemoteDataSource,
    private val dbConverter: CurrencyListDBConverter,
    private val dtoConverter: CurrencyListDtoConverter,
) : SupportedCurrenciesDataSource {
    private val currenciesCacheMutex = Mutex()

    override suspend fun getCurrencies(): SupportedCurrenciesResult {
        val cached = readCache()

        if (cached != null && isFresh(cached)) {
            return SupportedCurrenciesResult.Available(
                currencies = cached.currencies, isStale = false
            )
        }

        return currenciesCacheMutex.withLock {
            // Повторное чтение после ожидания (кеш мог обновиться)
            val currentCache = readCache()

            if (currentCache != null && isFresh(currentCache)) {
                return@withLock SupportedCurrenciesResult.Available(
                    currencies = currentCache.currencies, isStale = false
                )
            }

            when (val refreshed = refreshCurrenciesInternal()) {
                is SupportedCurrenciesResult.Available -> refreshed

                is SupportedCurrenciesResult.Unavailable -> {
                    if (currentCache != null) {
                        SupportedCurrenciesResult.Available(
                            currencies = currentCache.currencies, isStale = true
                        )
                    } else {
                        refreshed
                    }
                }
            }
        }
    }

    override suspend fun refreshCurrencies(): SupportedCurrenciesResult {
        return currenciesCacheMutex.withLock {
            refreshCurrenciesInternal()
        }
    }

    private suspend fun refreshCurrenciesInternal(): SupportedCurrenciesResult {
        return when (val result = remoteDataSource.getCurrencies()) {
            is NetworkResult.Success -> {
                processRemoteCurrencies(result.data)
            }

            is NetworkResult.HttpError -> {
                SupportedCurrenciesResult.Unavailable(
                    if (result.code in HTTP_CODE_400..HTTP_CODE_499) {
                        CurrencyListFailure.BadRequest
                    } else {
                        CurrencyListFailure.NetworkError
                    }
                )
            }

            is NetworkResult.ConnectionError -> {
                SupportedCurrenciesResult.Unavailable(
                    CurrencyListFailure.NetworkConnectionError
                )
            }

            is NetworkResult.UnexpectedError -> {
                SupportedCurrenciesResult.Unavailable(
                    CurrencyListFailure.UnknownNetworkError
                )
            }
        }
    }

    private suspend fun processRemoteCurrencies(currencies: List<CurrencyDto>)
            : SupportedCurrenciesResult {
        val knownCodes = Currency.entries
            .mapTo(mutableSetOf()) { it.isoCode.uppercase() }

        val supportedDtos = currencies.filter { currencyDto ->
            currencyDto.isoCode.trim().uppercase() in knownCodes
        }.map { currencyDto ->
            if (currencyDto.endDate == LocalDate.now().toString()) {
                currencyDto.copy(endDate = null)
            } else {
                currencyDto
            }
        }

        val entities = dtoConverter.mapToEntity(supportedDtos)
        val domainCurrencies = dtoConverter.map(supportedDtos)

        return try {
            database.withTransaction {
                currencyListDao.deleteAll()
                currencyListDao.insertAll(entities)
                currencyListDao.upsertSyncInfo(
                    CurrencyListSyncEntity(
                        lastSuccessfulSyncEpochDay = LocalDate.now().toEpochDay()
                    )
                )
            }

            SupportedCurrenciesResult.Available(
                currencies = domainCurrencies,
                isStale = false
            )
        } catch (e: SQLiteException) {
            Timber.e(e, "Failed to update supported currencies")
            SupportedCurrenciesResult.Unavailable(
                CurrencyListFailure.DatabaseError
            )
        }
    }

    private suspend fun readCache(): CachedCurrencies? {
        return try {
            val syncInfo = currencyListDao.getSyncInfo() ?: return null

            val entities = currencyListDao.getCurrencyList()

            CachedCurrencies(
                currencies = dbConverter.map(entities),
                lastSyncEpochDay = syncInfo.lastSuccessfulSyncEpochDay,
            )
        } catch (e: SQLiteException) {
            Timber.e(e, "Failed to read supported currencies")
            null
        }
    }

    private data class CachedCurrencies(
        val currencies: List<Currency>,
        val lastSyncEpochDay: Long
    )

    private fun isFresh(cache: CachedCurrencies): Boolean {
        return LocalDate.now().toEpochDay() - cache.lastSyncEpochDay < CACHE_TTL_DAYS
    }

    companion object {
        private const val CACHE_TTL_DAYS = 7L
        private const val HTTP_CODE_400 = 400
        private const val HTTP_CODE_499 = 499
    }
}
