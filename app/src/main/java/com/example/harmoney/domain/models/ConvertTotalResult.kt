package com.example.harmoney.domain.models

data class ConvertTotalResult(
    val total: ConvertedTotal,
    val exchangeRates: Set<ExchangeRateStatus>,
)
