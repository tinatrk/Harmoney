package com.example.harmoney.presentation.converters

import com.example.harmoney.domain.models.Currency
import com.example.harmoney.domain.models.TransactionsPerDay
import com.example.harmoney.presentation.models.TransactionsPerDayUi
import kotlinx.collections.immutable.ImmutableList

interface OneDayTransactionsUiConverter {
    fun map(day: TransactionsPerDay, currency: Currency): TransactionsPerDayUi

    fun map(days: List<TransactionsPerDay>, currency: Currency): ImmutableList<TransactionsPerDayUi>
}
