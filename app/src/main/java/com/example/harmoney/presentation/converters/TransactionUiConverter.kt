package com.example.harmoney.presentation.converters

import com.example.harmoney.domain.models.Currency
import com.example.harmoney.domain.models.Transaction
import com.example.harmoney.presentation.models.TransactionUi
import kotlinx.collections.immutable.ImmutableList

interface TransactionUiConverter {
    fun map(transaction: Transaction, currency: Currency) : TransactionUi

    fun map(transactions: List<Transaction>, currency: Currency): ImmutableList<TransactionUi>
}
