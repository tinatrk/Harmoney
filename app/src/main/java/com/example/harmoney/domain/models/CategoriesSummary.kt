package com.example.harmoney.domain.models

data class CategoriesSummary(
    val categories: List<CategoryStatistics>,
    val totalAmounts: List<Money>,
)
