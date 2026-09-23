package com.example.harmoney.domain.models

data class CategoryStatistics(
    val category: Category,
    val totalAmounts: List<Money>,
    //val percentage: Float,
)
