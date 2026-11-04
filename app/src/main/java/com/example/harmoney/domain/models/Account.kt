package com.example.harmoney.domain.models

import java.time.Instant

data class Account(
    val id: Long = 0L,
    val name: String,
    val currency: Currency,
    val initialBalance: MoneyAmount,
    val currentBalance: MoneyAmount,
    val isArchived: Boolean = false,
    val createdAt: Instant,
    val userOrder: Double = 0.0,
)
