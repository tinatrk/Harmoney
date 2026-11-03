package com.example.harmoney.data.currencyList

import com.example.harmoney.core.util.Resource
import com.example.harmoney.data.currencyList.network.SupportedCurrenciesDataSource
import com.example.harmoney.data.currencyList.network.SupportedCurrenciesResult
import com.example.harmoney.domain.currencyList.api.repository.CurrencyListRepository
import com.example.harmoney.domain.currencyList.models.CurrencyListFailure
import com.example.harmoney.domain.models.Currency

class CurrencyListRepositoryImpl(
    private val supportedCurrenciesDataSource: SupportedCurrenciesDataSource,
) : CurrencyListRepository {

    override suspend fun getSupportedCurrencyList(): Resource<List<Currency>, CurrencyListFailure> {
        return supportedCurrenciesDataSource.getCurrencies().toResource()
    }

    override suspend fun getRefreshedSupportedCurrencyList()
            : Resource<List<Currency>, CurrencyListFailure> {
        return supportedCurrenciesDataSource.refreshCurrencies().toResource()
    }

    private fun SupportedCurrenciesResult.toResource()
            : Resource<List<Currency>, CurrencyListFailure> {
        return when (this) {
            is SupportedCurrenciesResult.Available -> Resource.Success(currencies)
            is SupportedCurrenciesResult.Unavailable -> Resource.Error(failure)
        }
    }
}
