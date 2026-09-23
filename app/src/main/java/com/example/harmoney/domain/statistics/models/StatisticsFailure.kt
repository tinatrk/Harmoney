package com.example.harmoney.domain.statistics.models

import com.example.harmoney.domain.exchangeRate.models.ExchangeRateFailure
import com.example.harmoney.domain.transaction.models.TransactionFailure

sealed interface StatisticsFailure {
    data class Transaction(val failure: TransactionFailure) : StatisticsFailure
    data class ExchangeRate(val failure: ExchangeRateFailure) : StatisticsFailure
}
