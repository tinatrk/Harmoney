package com.example.harmoney.domain.models

sealed interface ExchangeRateResult {
    val source: Currency
    val target: Currency

    data class Available(val rate: ExchangeRate) : ExchangeRateResult {
        override val source: Currency
            get() = rate.sourceCurrency
        override val target: Currency
            get() = rate.targetCurrency
    }

    data class NotRequired(
        override val source: Currency,
        override val target: Currency
    ) : ExchangeRateResult

    data class NotAvailable(
        override val source: Currency,
        override val target: Currency
    ) : ExchangeRateResult
}
