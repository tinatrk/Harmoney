package com.example.harmoney.data.transaction.converter

import com.example.harmoney.data.core.bd.dto.TransactionDetailsDto
import com.example.harmoney.data.transaction.entity.TransactionEntity
import com.example.harmoney.domain.models.Transaction

interface TransactionDBConverter {
    fun map(transaction: Transaction): TransactionEntity

    fun map(transaction: TransactionDetailsDto): Transaction

    fun map(transactions: List<TransactionDetailsDto>): List<Transaction>
}
