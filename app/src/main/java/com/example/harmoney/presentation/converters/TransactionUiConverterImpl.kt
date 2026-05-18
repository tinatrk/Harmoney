package com.example.harmoney.presentation.converters

import com.example.harmoney.domain.models.Currency
import com.example.harmoney.domain.models.Transaction
import com.example.harmoney.presentation.models.TransactionUi
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

class TransactionUiConverterImpl(
    private val categoryUiConverter: CategoryUiConverter,
    private val numbersFormatter: NumbersFormatter,
) : TransactionUiConverter {
    override fun map(transaction: Transaction, currency: Currency): TransactionUi {
        return TransactionUi(
            id = transaction.id,
            category = categoryUiConverter.map(transaction.category),
            amount = numbersFormatter.toStringWithCurrency(
                number = transaction.amount,
                decimalPlaces = TWO_DECIMAL_PLACES,
                isNeededThousandSeparator = true,
                currency = currency
            ),
            note = transaction.note,
            createdAt = transaction.createdAt
        )
    }

    override fun map(
        transactions: List<Transaction>,
        currency: Currency
    ): ImmutableList<TransactionUi> {
        return transactions.map { map(it, currency) }.toImmutableList()
    }

    private companion object {
        const val TWO_DECIMAL_PLACES = 2
    }
}
