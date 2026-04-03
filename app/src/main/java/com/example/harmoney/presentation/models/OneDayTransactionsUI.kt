package com.example.harmoney.presentation.models

data class OneDayTransactionsUI(
    val data: String,
    val transactions: List<TransactionUi>,
    val totalAmount: Float,
)
