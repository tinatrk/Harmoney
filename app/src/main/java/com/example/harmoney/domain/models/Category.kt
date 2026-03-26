package com.example.harmoney.domain.models

data class Category(
    val id: Long = 0,
    val name: String,
    val type: CategoryType,
    val icon: CategoryIcon,
    val createdAt: Long = 0L,
    val userOrder: Double = 0.0
)
