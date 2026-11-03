package com.example.harmoney.domain.statistics.models

import com.example.harmoney.domain.models.Category
import com.example.harmoney.domain.models.Money

data class OriginalCategoryStatistics(
    val category: Category,
    val total: Money,
)
