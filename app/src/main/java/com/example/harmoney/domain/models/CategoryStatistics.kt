package com.example.harmoney.domain.models

data class CategoryStatistics(
    val category: Category,
    val totalAmount: Double,
    val percentage: Float,
)
