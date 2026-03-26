package com.example.harmoney.domain.models

import java.util.Date

data class Transaction(
    val id: Long = 0,
    val category: Category,
    val data: Date,
    val amount: Double,
    val note: String = ""
)
