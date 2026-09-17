package com.example.harmoney.domain.currencyList.models

sealed interface CurrencyListFailure {
    data object BadRequest: CurrencyListFailure
    data object NetworkConnectionError: CurrencyListFailure
    data object NetworkError: CurrencyListFailure
    data object UnknownNetworkError: CurrencyListFailure
}
