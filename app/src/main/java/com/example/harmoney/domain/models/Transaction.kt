package com.example.harmoney.domain.models

import java.time.LocalDate

data class Transaction(
    val id: Long = 0,
    val category: Category,
    val date: LocalDate,
    val amount: Money,
    val note: String = "",
    val createdAt: Long = 0L,
)
