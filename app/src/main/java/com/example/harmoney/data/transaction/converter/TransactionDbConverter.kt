package com.example.harmoney.data.transaction.converter

import com.example.harmoney.data.core.TransactionWithCategory
import com.example.harmoney.data.transaction.entity.TransactionEntity
import com.example.harmoney.domain.models.Transaction

interface TransactionDbConverter {
    fun map(transaction: Transaction): TransactionEntity

    fun map(transactionWithCategory: TransactionWithCategory): Transaction
}
