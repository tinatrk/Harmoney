package com.example.harmoney.data.transaction.converter

import com.example.harmoney.data.category.converter.CategoryDBConverter
import com.example.harmoney.data.converters.DateConverter
import com.example.harmoney.data.core.bd.dto.TransactionDetailsDto
import com.example.harmoney.data.transaction.entity.TransactionEntity
import com.example.harmoney.domain.models.MoneyAmount
import com.example.harmoney.domain.models.Transaction

class TransactionDBConverterImpl(
    private val dateConverter: DateConverter,
    private val categoryDBConverter: CategoryDBConverter,
) : TransactionDBConverter {
    override fun map(transaction: Transaction): TransactionEntity {
        return TransactionEntity(
            id = transaction.id,
            accountId = transaction.accountId,
            categoryId = transaction.category.id,
            dateMillis = dateConverter.dateToMillis(transaction.date),
            moneyAmountMinorUnits = transaction.amount.minorUnits,
            note = transaction.note,
            createdAt = transaction.createdAt,
        )
    }

    override fun map(transaction: TransactionDetailsDto): Transaction {
        return Transaction(
            id = transaction.transaction.id,
            accountId = transaction.account.id,
            category = categoryDBConverter.map(transaction.category),
            date = dateConverter.millisToDate(transaction.transaction.dateMillis),
            amount = MoneyAmount(transaction.transaction.moneyAmountMinorUnits),
            note = transaction.transaction.note,
            createdAt = transaction.transaction.createdAt
        )
    }

    override fun map(transactions: List<TransactionDetailsDto>): List<Transaction> {
        return transactions.map { map(it) }
    }
}
