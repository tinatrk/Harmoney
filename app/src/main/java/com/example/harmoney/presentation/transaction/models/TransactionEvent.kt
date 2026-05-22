package com.example.harmoney.presentation.transaction.models

import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.domain.models.Currency
import java.time.LocalDate

sealed interface TransactionEvent {
    data object OnBackClick : TransactionEvent
    data object OnBackDialogConfirm: TransactionEvent
    data object OnBackDialogDismiss: TransactionEvent
    data class OnTabClick(val categoryType: CategoryType) : TransactionEvent

    data object OnDateDialogOpen: TransactionEvent
    data class OnDateDialogConfirm(val newDate: LocalDate): TransactionEvent
    data object OnDateDialogDismiss : TransactionEvent
    data object OnDateErrorDialogDismiss: TransactionEvent

    data object OnCalculatorOpen : TransactionEvent
    data class OnAmountChanged(val newAmount: Double) : TransactionEvent
    data object OnCalculatorDismiss : TransactionEvent
    data object OnCurrencyClick : TransactionEvent
    data class OnCurrencyChanged(val newCurrency: Currency) : TransactionEvent
    data object OnCurrencyDismiss: TransactionEvent

    data class OnNoteChanged(val newNote: String) : TransactionEvent

    data class OnCategoryClick(val categoryId: Long) : TransactionEvent
    data object OnMoreCategoriesClick : TransactionEvent

    data object OnSaveClick : TransactionEvent
    data object OnSaveDialogDismiss: TransactionEvent
    data object OnCloseScreen : TransactionEvent
}
