package com.example.harmoney.domain.models

data class Transaction(
    val id: Long = 0,
    val category: Category,
    val dateMillis: Long,
    val amount: Double,
    val note: String = "",
    val createdAt: Long = 0L,
)
