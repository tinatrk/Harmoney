package com.example.harmoney.presentation.models

import androidx.compose.runtime.Immutable

@Immutable
data class CategoryStatisticsUi(
    val category: CategoryUi,
    val totalAmount: String,
    val percentage: String,
)
