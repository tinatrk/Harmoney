package com.example.harmoney.presentation.transaction.models

import androidx.compose.runtime.Stable
import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.domain.models.Currency
import com.example.harmoney.presentation.models.CategoryUi
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@Stable
data class TransactionState(
    val isCreateTransactionScreen: Boolean = true,

    val categoryTypes: ImmutableList<CategoryType> = CategoryType.entries.toImmutableList(),
    val selectedCategoryType: CategoryType = CategoryType.Expenses,
    val selectedTabIndex: Int = CategoryType.Expenses.ordinal,

    val selectedDate: String = "",
    //val selectedDateMillis: Long = 0,
    val isDatePickerOpened: Boolean = false,
    val isDateErrorDialogOpen: Boolean = false,
    val dateError: TransactionDateError = TransactionDateError.None,

    val amountInLocalCurrency: String = "",
    val amountInGlobalCurrency: String = "",
    val isUsedCurrencyExchange: Boolean = false,
    val isCalculatorOpen: Boolean = false,
    val amountError: TransactionAmountError = TransactionAmountError.None,
    val isCurrencyMenuOpened: Boolean = false,
    val globalCurrency: Currency = Currency.RUB,
    val localCurrency: Currency = Currency.RUB,

    val note: String = "",

    val categories: ImmutableList<CategoryUi> = persistentListOf(),
    val selectedCategoryId: Long? = null,
    val isCategoryError: Boolean = false,

    val isSaveTransactionErrorDialogOpened: Boolean = false,
    val isTransactionNotSavedDialogOpened: Boolean = false,
)
