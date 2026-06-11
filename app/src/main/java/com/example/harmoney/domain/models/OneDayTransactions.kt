package com.example.harmoney.domain.models

data class OneDayTransactions(
    val dateMillis: Long,
    val transactions: List<Transaction>,
    val totalAmount: Double,
)
