package com.example.harmoney.domain.models

import java.time.LocalDate

data class OneDayTransactions(
    val date: LocalDate,
    val transactions: List<Transaction>,
    val totalAmount: Double,
)
