package com.example.harmoney.presentation.models

import androidx.compose.runtime.Immutable

@Immutable
data class CategoryUi(
    val id: Long = 0,
    val info: CategoryInfoUi,
    val totalAmount: Float,
    val percentage: Float,
)
