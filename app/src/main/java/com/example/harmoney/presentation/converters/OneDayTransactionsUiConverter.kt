package com.example.harmoney.presentation.converters

import com.example.harmoney.domain.models.Currency
import com.example.harmoney.domain.models.OneDayTransactions
import com.example.harmoney.presentation.models.OneDayTransactionsUi
import kotlinx.collections.immutable.ImmutableList

interface OneDayTransactionsUiConverter {
    fun map(day: OneDayTransactions, currency: Currency): OneDayTransactionsUi

    fun map(days: List<OneDayTransactions>, currency: Currency): ImmutableList<OneDayTransactionsUi>
}
