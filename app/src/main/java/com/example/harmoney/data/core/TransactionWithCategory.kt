package com.example.harmoney.data.core

import androidx.room.DatabaseView
import androidx.room.Embedded
import com.example.harmoney.data.category.entity.CategoryEntity
import com.example.harmoney.data.transaction.entity.TransactionEntity

@DatabaseView(
    viewName = "transaction_with_category_view",
    value = """
        SELECT `transaction`.*,
        category.id AS category_id,
        category.name AS category_name,
        category.typeId AS category_typeId,
        category.iconId AS category_iconId,
        category.iconColorId AS category_iconColorId,
        category.createdAt AS category_createdAt,
        category.userOrder AS category_userOrder
        FROM `transaction`
        INNER JOIN category ON `transaction`.categoryId = category.id
    """
)
data class TransactionWithCategory(
    @Embedded
    val transaction: TransactionEntity,
    @Embedded(prefix = "category_")
    val category: CategoryEntity
)
