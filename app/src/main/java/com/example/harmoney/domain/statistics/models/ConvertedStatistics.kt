package com.example.harmoney.domain.statistics.models

import com.example.harmoney.domain.models.ConvertedCategoryStatistics
import com.example.harmoney.domain.models.ConvertedTotal
import com.example.harmoney.domain.models.ExchangeRateStatus

data class ConvertedStatistics(
    val categories: List<ConvertedCategoryStatistics>,
    val total: ConvertedTotal,
    val exchangeRateStatuses: Set<ExchangeRateStatus>
)
