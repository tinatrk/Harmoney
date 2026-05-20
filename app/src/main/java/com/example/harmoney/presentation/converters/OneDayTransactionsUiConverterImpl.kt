package com.example.harmoney.presentation.converters

import com.example.harmoney.domain.models.Currency
import com.example.harmoney.domain.models.OneDayTransactions
import com.example.harmoney.presentation.models.OneDayTransactionsUi
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class OneDayTransactionsUiConverterImpl(
    private val transactionUiConverter: TransactionUiConverter,
    private val numberFormatter: NumbersFormatter
) : OneDayTransactionsUiConverter {
    override fun map(day: OneDayTransactions, currency: Currency): OneDayTransactionsUi {
        return OneDayTransactionsUi(
            date = dateToString(day.date),
            transactions = transactionUiConverter.map(day.transactions, currency),
            totalAmount = numberFormatter.toStringWithCurrency(
                number = day.totalAmount,
                decimalPlaces = TWO_DECIMAL_PLACES,
                currency = currency,
                isNeededThousandSeparator = true
            )
        )
    }

    override fun map(
        days: List<OneDayTransactions>,
        currency: Currency
    ): ImmutableList<OneDayTransactionsUi> {
        return days.map { map(it, currency) }.toImmutableList()
    }

    private fun dateToString(date: LocalDate): String {
        val formatter = DateTimeFormatter.ofPattern(DATE_PATTERN_2)
        val res = date.format(formatter)
        return res
    }

    private companion object {
        const val TWO_DECIMAL_PLACES = 2
        const val DATE_PATTERN_2 = "dd MMMM"
    }
}
