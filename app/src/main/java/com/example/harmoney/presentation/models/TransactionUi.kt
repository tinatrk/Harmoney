package com.example.harmoney.presentation.models

import androidx.compose.runtime.Immutable

@Immutable
data class TransactionUi(
    val id: Long = 0,
    val category: CategoryUi,
    val amount: String,
    val note: String = "",
    val createdAt: Long = 0L,
)
