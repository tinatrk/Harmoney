package com.example.harmoney.domain.statistics.models

import com.example.harmoney.domain.models.CategoryStatistics
import com.example.harmoney.domain.models.Money

data class RawStatistics(
    val categories: List<CategoryStatistics>,
    val total: List<Money>,
)
