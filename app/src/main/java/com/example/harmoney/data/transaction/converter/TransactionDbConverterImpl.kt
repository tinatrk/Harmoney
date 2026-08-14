package com.example.harmoney.data.transaction.converter

import com.example.harmoney.data.converters.DateConverter
import com.example.harmoney.data.core.TransactionWithCategory
import com.example.harmoney.data.transaction.entity.TransactionEntity
import com.example.harmoney.domain.models.Category
import com.example.harmoney.domain.models.CategoryColors
import com.example.harmoney.domain.models.CategoryIcon
import com.example.harmoney.domain.models.CategoryIcons
import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.domain.models.Transaction

class TransactionDbConverterImpl(private val dateConverter: DateConverter) :
    TransactionDbConverter {
    override fun map(transaction: Transaction): TransactionEntity {
        return TransactionEntity(
            id = transaction.id,
            categoryId = transaction.category.id,
            dateMillis = dateConverter.dateToMillis(transaction.date),
            amount = transaction.amount,
            note = transaction.note,
            createdAt = transaction.createdAt,
        )
    }

    override fun map(transactionWithCategory: TransactionWithCategory): Transaction {
        return Transaction(
            id = transactionWithCategory.transaction.id,
            category = Category(
                id = transactionWithCategory.category.id,
                name = transactionWithCategory.category.name,
                type = CategoryType.fromId(transactionWithCategory.category.typeId),
                icon = CategoryIcon(
                    icon = CategoryIcons.fromId(transactionWithCategory.category.iconId),
                    color = CategoryColors.fromId(transactionWithCategory.category.iconColorId)
                ),
                createdAt = transactionWithCategory.category.createdAt,
                userOrder = transactionWithCategory.category.userOrder
            ),
            date = dateConverter.millisToDate(transactionWithCategory.transaction.dateMillis),
            amount = transactionWithCategory.transaction.amount,
            note = transactionWithCategory.transaction.note,
            createdAt = transactionWithCategory.transaction.createdAt
        )
    }
}
