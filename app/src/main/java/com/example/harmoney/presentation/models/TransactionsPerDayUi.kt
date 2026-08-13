package com.example.harmoney.presentation.models

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class TransactionsPerDayUi(
    val date: String,
    val transactions: ImmutableList<TransactionUi>,
    val totalAmount: String,
)
