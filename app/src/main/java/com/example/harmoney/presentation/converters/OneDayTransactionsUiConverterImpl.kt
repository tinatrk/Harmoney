package com.example.harmoney.presentation.converters

import com.example.harmoney.domain.models.Currency
import com.example.harmoney.domain.models.OneDayTransactions
import com.example.harmoney.presentation.models.DecimalPlaces
import com.example.harmoney.presentation.models.OneDayTransactionsUi
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

class OneDayTransactionsUiConverterImpl(
    private val transactionUiConverter: TransactionUiConverter,
    private val numberFormatter: NumbersFormatter,
    private val dateFormatter: DateFormatter
) : OneDayTransactionsUiConverter {
    override fun map(day: OneDayTransactions, currency: Currency): OneDayTransactionsUi {
        return OneDayTransactionsUi(
            date = dateFormatter
                .formatShortDate(date = day.date),
            transactions = transactionUiConverter.map(day.transactions, currency),
            totalAmount = numberFormatter.toStringWithCurrency(
                number = day.totalAmount,
                decimalPlaces = DecimalPlaces.MONEY_DISPLAY,
                currency = currency,
                isNeededThousandSeparator = true
            )
        )
    }

    override fun map(
        days: List<OneDayTransactions>,
        currency: Currency
    ): ImmutableList<OneDayTransactionsUi> {
        return days.map { map(day = it, currency = currency) }.toImmutableList()
    }
}
