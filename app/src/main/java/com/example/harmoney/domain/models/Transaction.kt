package com.example.harmoney.domain.models

import java.time.LocalDate

data class Transaction(
    val id: Long = 0L,
    val accountId: Long,
    val category: Category,
    val date: LocalDate,
    val amount: MoneyAmount,
    val note: String = "",
    val createdAt: Long = 0L,
)
