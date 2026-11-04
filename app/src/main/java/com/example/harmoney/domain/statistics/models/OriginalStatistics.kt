package com.example.harmoney.domain.statistics.models

import com.example.harmoney.domain.models.Money

data class OriginalStatistics(
    val categories: List<OriginalCategoryStatistics>,
    val total: Money,
)
