package com.example.harmoney.presentation.transaction.models

import com.example.harmoney.domain.models.CategoryType

sealed interface TransactionEvent {
    data object OnBackClick : TransactionEvent
    data class OnTabClick(val categoryType: CategoryType) : TransactionEvent
    data object OnMoreCategoriesClick : TransactionEvent
    data object OnSaveClick : TransactionEvent
    data object OnCloseScreen : TransactionEvent
}
