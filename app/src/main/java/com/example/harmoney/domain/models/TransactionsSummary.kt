package com.example.harmoney.domain.models

data class TransactionsSummary(
    val transactions: List<Transaction>,
    val totalAmounts: List<Money>,
)
