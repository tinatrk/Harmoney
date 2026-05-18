package com.example.harmoney.presentation.converters

import com.example.harmoney.domain.models.Currency
import com.example.harmoney.domain.models.OneDayTransactions
import com.example.harmoney.presentation.models.OneDayTransactionsUI
import kotlinx.collections.immutable.ImmutableList

interface OneDayTransactionsUiConverter {
    fun map(day: OneDayTransactions, currency: Currency): OneDayTransactionsUI

    fun map(days: List<OneDayTransactions>, currency: Currency): ImmutableList<OneDayTransactionsUI>
}