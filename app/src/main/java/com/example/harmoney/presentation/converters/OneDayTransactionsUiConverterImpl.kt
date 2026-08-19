package com.example.harmoney.presentation.converters

import com.example.harmoney.domain.models.Currency
import com.example.harmoney.domain.models.TransactionsPerDay
import com.example.harmoney.presentation.models.TransactionsPerDayUi
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

class OneDayTransactionsUiConverterImpl(
    private val transactionUiConverter: TransactionUiConverter,
    private val numberFormatter: NumbersFormatter,
    private val dateFormatter: DateFormatter
) : OneDayTransactionsUiConverter {
    override fun map(day: TransactionsPerDay, currency: Currency): TransactionsPerDayUi {
        return TransactionsPerDayUi(
            date = dateFormatter
                .formatShortDate(date = day.date),
            transactions = transactionUiConverter.map(day.transactions, currency),
            totalAmount = numberFormatter.moneyToStringWithCurrency(
                moneyMinorUnits = day.totalAmount.minorUnits,
                currency = currency,
            )
        )
    }

    override fun map(
        days: List<TransactionsPerDay>,
        currency: Currency
    ): ImmutableList<TransactionsPerDayUi> {
        return days.map { map(day = it, currency = currency) }.toImmutableList()
    }
}
