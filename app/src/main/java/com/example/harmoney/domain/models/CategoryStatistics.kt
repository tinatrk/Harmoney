package com.example.harmoney.domain.models

data class CategoryStatistics(
    val category: Category,
    val totalAmount: Money,
    val percentage: Float,
)
