package com.example.harmoney.domain.models

data class ConvertCategoryStatisticsResult(
    val categoryStatistics: List<ConvertedCategoryStatistics>,
    val exchangeRates: Set<ExchangeRateStatus>
)
