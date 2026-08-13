package com.example.harmoney.presentation.transactionList.models

import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.domain.models.StatisticsPeriodType
import com.example.harmoney.presentation.models.TransactionFilterUi

sealed interface TransactionListEvent {
    data object OnBackClick : TransactionListEvent
    data class OnTabClick(val categoryType: CategoryType) : TransactionListEvent
    data class OnStatisticsPeriodClick(val newPeriod: StatisticsPeriodType) : TransactionListEvent
    data object OnFloatingButtonClick : TransactionListEvent
    data class OnTransactionClick(val transactionId: Long) : TransactionListEvent
    data object OnFilterMenuClick : TransactionListEvent
    data class OnFilterMenuChanged(val filter: TransactionFilterUi) : TransactionListEvent
    data object OnFilterMenuDismiss : TransactionListEvent
}
