package com.example.harmoney.presentation.models

import com.example.harmoney.domain.models.Currency
import java.math.BigDecimal
import java.time.Instant

data class AccountUi(
    val id: Long,
    val name: String,
    val currency: Currency,
    val initialBalance: BigDecimal,
    val currentBalance: BigDecimal,
    val isArchived: Boolean,
    val createdAt: Instant,
    val userOrder: Double = 0.0,
)
