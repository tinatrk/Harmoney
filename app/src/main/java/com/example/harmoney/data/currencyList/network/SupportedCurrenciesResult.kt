package com.example.harmoney.data.currencyList.network

import com.example.harmoney.domain.currencyList.models.CurrencyListFailure
import com.example.harmoney.domain.models.Currency


sealed interface SupportedCurrenciesResult {
    data class Available(
        val currencies: List<Currency>,
        val isStale: Boolean,
    ) : SupportedCurrenciesResult

    data class Unavailable(
        val failure: CurrencyListFailure
    ) : SupportedCurrenciesResult
}
