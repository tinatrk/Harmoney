package com.example.harmoney.data.transaction.dto

data class CategoryStatisticsDb(
    val id: Long,
    val name: String,
    val typeId: Long,
    val iconId: Long,
    val iconColorId: Long,
    val createdAt: Long,
    val userOrder: Double,
    val totalAmount: Long,
    val percentage: Float,
)
