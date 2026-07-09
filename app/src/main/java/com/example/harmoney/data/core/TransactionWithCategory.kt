package com.example.harmoney.data.core

import androidx.room.Embedded
import com.example.harmoney.data.category.entity.CategoryEntity
import com.example.harmoney.data.transaction.entity.TransactionEntity

data class TransactionWithCategory(
    @Embedded
    val transaction: TransactionEntity,
    @Embedded(prefix = "category_")
    val category: CategoryEntity
)
