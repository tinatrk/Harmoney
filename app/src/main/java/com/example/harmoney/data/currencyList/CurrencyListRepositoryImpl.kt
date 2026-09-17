package com.example.harmoney.data.currencyList

import android.util.Log
import androidx.sqlite.SQLiteException
import com.example.harmoney.core.util.Resource
import com.example.harmoney.data.currencyList.db.converter.CurrencyListDBConverter
import com.example.harmoney.data.currencyList.db.dao.CurrencyListDao
import com.example.harmoney.data.currencyList.db.entity.CurrencyEntity
import com.example.harmoney.data.currencyList.network.converter.CurrencyListDtoConverter
import com.example.harmoney.data.currencyList.network.dto.CurrencyDto
import com.example.harmoney.data.network.RemoteDataSource
import com.example.harmoney.data.network.NetworkResult
import com.example.harmoney.domain.currencyList.api.repository.CurrencyListRepository
import com.example.harmoney.domain.currencyList.models.CurrencyListFailure
import com.example.harmoney.domain.models.Currency
import timber.log.Timber
import java.time.LocalDate

class CurrencyListRepositoryImpl(
    private val currencyListDao: CurrencyListDao,
    private val remoteDataSource: RemoteDataSource,
    private val currencyListDBConverter: CurrencyListDBConverter,
    private val currencyListDtoConverter: CurrencyListDtoConverter,
) : CurrencyListRepository {

    override suspend fun getSupportedCurrencyList(): Resource<List<Currency>, CurrencyListFailure> {
        val list = loadSupportedCurrenciesFromCache()

        return if (list.isNotEmpty()) {
            Resource.Success(list)
        } else {
            getRefreshedSupportedCurrencyList()
        }
    }

    override suspend fun getRefreshedSupportedCurrencyList()
            : Resource<List<Currency>, CurrencyListFailure> {

        return when (val result = remoteDataSource.getCurrencies()) {
            is NetworkResult.Success -> {
                processCurrencies(result.data)
            }

            is NetworkResult.HttpError -> {
                when (result.code) {
                    in 400..499 -> Resource.Error(CurrencyListFailure.BadRequest)
                    else -> Resource.Error(CurrencyListFailure.NetworkError)
                }
            }

            is NetworkResult.ConnectionError -> {
                Resource.Error(CurrencyListFailure.NetworkConnectionError)
            }

            is NetworkResult.UnexpectedError -> {
                Resource.Error(CurrencyListFailure.UnknownNetworkError)
            }
        }
    }

    private suspend fun loadSupportedCurrenciesFromCache(): List<Currency> {
        return try {
            val list = currencyListDao.getCurrencyList()
            currencyListDBConverter.map(list)
        } catch (e: SQLiteException) {
            Timber.e(e, "Error getting currency list (cache)")
            emptyList()
        }
    }

    private suspend fun updateSupportedCurrenciesCache(currencies: List<CurrencyEntity>) {
        try {
            currencyListDao.deleteAll()
            currencyListDao.insertAll(currencies)
        } catch (e: SQLiteException) {
            Timber.e(e, "Error updating currency list (cache)")
        }
    }

    private suspend fun processCurrencies(currencies: List<CurrencyDto>)
    : Resource<List<Currency>, CurrencyListFailure> {
        val supportedCurrenciesDto =
            currencies.filter { currencyDto ->
                currencyDto
                    .isoCode.trim().uppercase() in Currency.entries.map { it.isoCode }
            }.map { currencyDto ->
                val date = currencyDto.endDate?.let(LocalDate::parse)
                if (date == LocalDate.now()) {
                    currencyDto.copy(endDate = null)
                } else {
                    currencyDto
                }
            }

        updateSupportedCurrenciesCache(
            currencies = currencyListDtoConverter
                .mapToEntity(supportedCurrenciesDto)
        )

        return Resource.Success(
            data = currencyListDtoConverter.map(currencies = supportedCurrenciesDto)
        )
    }
}
