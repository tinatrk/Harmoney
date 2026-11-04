package com.example.harmoney.domain.currencyList.api.repository

import com.example.harmoney.core.util.Resource
import com.example.harmoney.domain.currencyList.models.CurrencyListFailure
import com.example.harmoney.domain.models.Currency

interface CurrencyListRepository {
    suspend fun getSupportedCurrencyList(): Resource<List<Currency>, CurrencyListFailure>

    suspend fun getRefreshedSupportedCurrencyList(): Resource<List<Currency>, CurrencyListFailure>
}
