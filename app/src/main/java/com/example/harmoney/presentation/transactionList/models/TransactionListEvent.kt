package com.example.harmoney.presentation.transactionList.models

import com.example.harmoney.domain.models.CategoryType

sealed interface TransactionListEvent {
    data object OnBackClick : TransactionListEvent
    data class OnTabClick(val categoryType: CategoryType) : TransactionListEvent
    data class OnStatisticsPeriodClick(val newPeriodId: Long) : TransactionListEvent
    data class OnFloatingButtonClick(val categoryId: Long?) : TransactionListEvent
    data class OnTransactionClick(val transactionId: Long) : TransactionListEvent
}
