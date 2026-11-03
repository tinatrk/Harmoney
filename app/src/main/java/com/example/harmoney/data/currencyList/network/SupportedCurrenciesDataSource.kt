package com.example.harmoney.data.currencyList.network

interface SupportedCurrenciesDataSource {
    suspend fun getCurrencies(): SupportedCurrenciesResult

    suspend fun refreshCurrencies(): SupportedCurrenciesResult
}
