package com.example.harmoney.domain.exchangeRate.models

import com.example.harmoney.core.util.Failure

sealed interface ExchangeRateFailure : Failure {
    data object BadRequest : ExchangeRateFailure
    data object DatabaseError: ExchangeRateFailure
    data object NetworkConnectionError: ExchangeRateFailure
    data object NetworkError: ExchangeRateFailure
    data object RateNotAvailable: ExchangeRateFailure
}
