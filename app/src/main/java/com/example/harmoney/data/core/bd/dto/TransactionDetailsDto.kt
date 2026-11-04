package com.example.harmoney.data.core.bd.dto

import androidx.room.DatabaseView
import androidx.room.Embedded
import com.example.harmoney.data.account.entity.AccountEntity
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
        category.userOrder AS category_userOrder,
        
        account.id AS account_id,
        account.name AS account_name,
        account.currencyId AS account_currencyId,
        account.initialBalanceMinorUnits AS account_initialBalanceMinorUnits,
        account.isArchived AS account_isArchived,
        account.createdAtMillis AS account_createdAtMillis,
        account.userOrder AS account_userOrder
        
        FROM `transaction`
        INNER JOIN category ON `transaction`.categoryId = category.id
        INNER JOIN account ON `transaction`.accountId = account.id
    """
)
data class TransactionDetailsDto(
    @Embedded
    val transaction: TransactionEntity,
    @Embedded(prefix = "account_")
    val account: AccountEntity,
    @Embedded(prefix = "category_")
    val category: CategoryEntity,
)
// точно ли здесь нужны другие поля account кроме accountId?
