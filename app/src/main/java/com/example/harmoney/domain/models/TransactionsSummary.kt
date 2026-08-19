package com.example.harmoney.domain.models

data class TransactionsSummary(
    val days: List<TransactionsPerDay>,
    val totalAmount: Money
)
