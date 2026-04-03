package com.example.harmoney.presentation.models

import androidx.compose.runtime.Immutable

@Immutable
data class TransactionUi(
    val id: Long = 0,
    val category: CategoryInfoUi,
    val amount: Float,
    val note: String = ""
)
