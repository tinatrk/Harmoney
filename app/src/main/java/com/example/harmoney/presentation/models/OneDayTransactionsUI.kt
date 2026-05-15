package com.example.harmoney.presentation.models

data class OneDayTransactionsUI(
    val date: String,
    val transactions: List<TransactionUi>,
    val totalAmount: String,
)
