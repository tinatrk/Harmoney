package com.example.harmoney.presentation.transaction.viewModel

import com.example.harmoney.base.BaseViewModel
import com.example.harmoney.domain.models.Category
import com.example.harmoney.domain.models.CategoryColors
import com.example.harmoney.domain.models.CategoryIcon
import com.example.harmoney.domain.models.CategoryIcons
import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.domain.models.Currency
import com.example.harmoney.domain.models.Transaction
import com.example.harmoney.presentation.converters.CategoryUiConverter
import com.example.harmoney.presentation.converters.DateFormatter
import com.example.harmoney.presentation.converters.NumbersFormatter
import com.example.harmoney.presentation.converters.TransactionUiConverter
import com.example.harmoney.presentation.models.DatePattern
import com.example.harmoney.presentation.models.DecimalPlaces
import com.example.harmoney.presentation.models.TransactionUi
import com.example.harmoney.presentation.test.TestDataSource
import com.example.harmoney.presentation.transaction.models.TransactionAction
import com.example.harmoney.presentation.transaction.models.TransactionAmountError
import com.example.harmoney.presentation.transaction.models.TransactionDateError
import com.example.harmoney.presentation.transaction.models.TransactionEvent
import com.example.harmoney.presentation.transaction.models.TransactionState
import kotlinx.coroutines.flow.update
import java.time.LocalDate

@Suppress("detekt:LongParameterList", "detekt:TooManyFunctions")
class TransactionViewModel(
    categoryType: CategoryType,
    categoryId: Long?,
    transactionId: Long?,
    private val test: TestDataSource,
    private val numbersFormatter: NumbersFormatter,
    private val dateFormatter: DateFormatter,
    private val transactionUiConverter: TransactionUiConverter,
    private val categoryUiConverter: CategoryUiConverter
) : BaseViewModel<TransactionEvent, TransactionAction, TransactionState>(
    state =
        TransactionState(
            isCreateTransactionScreen = transactionId == null,
            selectedCategoryType = categoryType,
            selectedTabIndex = categoryType.ordinal,
        )
) {
    override val tag: String = TransactionViewModel::class.java.simpleName ?: ""

    private val initialTransactionUi: TransactionUi
    private var curTransaction: Transaction
    private var localAmount: Double = 0.0

    private val selectedCategories: MutableList<Long> =
        CategoryType.entries.map { ZERO_ID }.toMutableList()

    private val firstDayMillis = test.getFirstDay()
    private val lastDayMillis = test.getLastDay()

    init {
        // TODO() в будущем считывать валюту из sharedPreferences

        val categories = test.getCategories(state.value.selectedCategoryType)

        curTransaction = getInitialTransaction(transactionId, categories, categoryId)
        initialTransactionUi =
            transactionUiConverter.map(curTransaction, state.value.globalCurrency)
        localAmount = curTransaction.amount

        selectedCategories[state.value.selectedCategoryType.ordinal] = curTransaction.category.id

        val amount = numbersFormatter.toString(
            number = curTransaction.amount,
            decimalPlaces = DecimalPlaces.MONEY_DISPLAY,
            isNeededThousandSeparator = true
        )

        writableState.update {
            it.copy(
                isCreateTransactionScreen = initialTransactionUi.id == ZERO_ID,
                selectedDate = dateFormatter.millisToString(
                    dateMillis = curTransaction.dateMillis,
                    pattern = DatePattern.CARD_FULLY
                ),
                localCurrency = it.globalCurrency,
                amountInLocalCurrency = amount,
                amountInGlobalCurrency = amount,
                amountError = TransactionAmountError.None,
                note = curTransaction.note,
                categories = categoryUiConverter.map(categories),
                selectedCategory = if (curTransaction.category.id != ZERO_ID) {
                    categoryUiConverter.map(curTransaction.category)
                } else {
                    null
                }
            )
        }
    }

    @Suppress("detekt:CyclomaticComplexMethod")
    override fun obtainEvent(event: TransactionEvent) {
        when (event) {
            is TransactionEvent.OnBackClick -> onBackClick()
            is TransactionEvent.OnBackDialogConfirm -> onBackDialogConfirm()
            is TransactionEvent.OnBackDialogDismiss -> onBackDialogDismiss()

            is TransactionEvent.OnTabClick -> onTabClick(event.categoryType)

            is TransactionEvent.OnDateDialogOpen -> onDateDialogOpen()
            is TransactionEvent.OnDateDialogConfirm -> onDateDialogConfirm(event.newDateMillis)
            is TransactionEvent.OnDateDialogDismiss -> onDateDialogDismiss()

            is TransactionEvent.OnCalculatorOpen -> onCalculatorOpen()
            is TransactionEvent.OnAmountChanged -> onAmountChange(event.newAmount)
            is TransactionEvent.OnCalculatorDismiss -> onCalculatorDismiss()

            is TransactionEvent.OnCurrencyClick -> onCurrencyClick()
            is TransactionEvent.OnCurrencyChanged -> onCurrencyChanged(event.newCurrency)
            is TransactionEvent.OnCurrencyDismiss -> onCurrencyDismiss()

            is TransactionEvent.OnNoteChanged -> onNoteChanged(event.newNote)

            is TransactionEvent.OnCategoriesBottomSheetOpened -> onCategoriesBottomSheetOpened()
            is TransactionEvent.OnCategoriesBottomSheetDismiss -> onCategoriesBottomSheetDismiss()
            is TransactionEvent.OnCategoryClick -> onCategoryClick(newCategoryId = event.categoryId)
            is TransactionEvent.OnCreateCategoryClick -> onNavigateToCategoryScreen()

            is TransactionEvent.OnSaveClick -> onSaveClick()
            is TransactionEvent.OnSaveDialogDismiss -> onSaveDialogDismiss()

            is TransactionEvent.OnDeleteClick -> onDeleteClick()
            is TransactionEvent.OnDeleteDialogConfirm -> onDeleteDialogConfirm()
            is TransactionEvent.OnDeleteDialogDismiss -> onDeleteDialogDismiss()
        }
    }

    private fun onBackClick() {
        val curTransactionUi =
            transactionUiConverter.map(curTransaction, state.value.globalCurrency)

        if (initialTransactionUi != curTransactionUi) {
            writableState.update { it.copy(isTransactionNotSavedDialogOpened = true) }
        } else {
            onNavigateBack()
        }
    }

    private fun onBackDialogConfirm() {
        writableState.update { it.copy(isTransactionNotSavedDialogOpened = false) }
        onNavigateBack()
    }

    private fun onBackDialogDismiss() {
        writableState.update { it.copy(isTransactionNotSavedDialogOpened = false) }
    }

    private fun onNavigateBack() {
        writableAction.tryEmit(TransactionAction.NavigateBack)
    }

    private fun onTabClick(newCategoryType: CategoryType) {
        if (state.value.selectedCategoryType.id != newCategoryType.id) {
            val categories = test.getCategories(newCategoryType)
            val oldCategoryId = selectedCategories[newCategoryType.ordinal]
            val newCategoryId = when {
                categories.isEmpty() -> null
                oldCategoryId == ZERO_ID -> categories.first().id
                oldCategoryId != ZERO_ID -> categories.find { it.id == oldCategoryId }?.id
                else -> null
            }

            val curCategory = categories.find { it.id == newCategoryId }

            curTransaction =
                curTransaction.copy(category = curCategory ?: getEmptyCategory(newCategoryType))

            writableState.update {
                it.copy(
                    selectedCategoryType = newCategoryType,
                    selectedTabIndex = newCategoryType.ordinal,
                    categories = categoryUiConverter.map(categories),
                    selectedCategory = curCategory?.let { categoryUiConverter.map(curCategory) }
                )
            }
        }
    }

    private fun onDateDialogOpen() {
        writableState.update { it.copy(isDatePickerOpened = true) }
    }

    private fun onDateDialogConfirm(newDateMillis: Long?) {
        val dateMillis = newDateMillis ?: dateFormatter.dateToMillis(LocalDate.now())
        val isDateCorrect = dateMillis in firstDayMillis..lastDayMillis

        curTransaction = curTransaction.copy(dateMillis = dateMillis)

        writableState.update {
            it.copy(
                dateError = if (isDateCorrect) {
                    TransactionDateError.None
                } else {
                    TransactionDateError.OutOfRange(
                        firstDay = dateFormatter.millisToString(
                            dateMillis = firstDayMillis,
                            pattern = DatePattern.CARD_FULLY
                        ),
                        lastDay = dateFormatter.millisToString(
                            dateMillis = lastDayMillis,
                            pattern = DatePattern.CARD_FULLY
                        ),
                    )
                },
                isDatePickerOpened = !isDateCorrect,
                selectedDate = dateFormatter.millisToString(
                    dateMillis = dateMillis,
                    pattern = DatePattern.CARD_FULLY
                ),
            )
        }
    }

    private fun onDateDialogDismiss() {
        writableState.update { it.copy(isDatePickerOpened = false) }
    }

    private fun onCalculatorOpen() {
        writableState.update { it.copy(isCalculatorOpen = true) }
    }

    private fun onAmountChange(newAmount: Double) {
        if (curTransaction.amount == newAmount) return

        // преобразовать валюты, считав актуальный курс из интеренета по API
        val globalAmount = if (state.value.isUsedCurrencyExchange) {
            test.getAmountAfterCurrencyExchanged(
                localCurrency = state.value.localCurrency,
                targetCurrency = state.value.globalCurrency,
                localAmount = newAmount
            )
        } else {
            newAmount
        }

        val globalAmountString = numbersFormatter.toString(
            number = globalAmount,
            decimalPlaces = DecimalPlaces.MONEY_DISPLAY,
            isNeededThousandSeparator = true
        )

        val localAmountString = if (state.value.isUsedCurrencyExchange) {
            numbersFormatter.toString(
                number = newAmount,
                decimalPlaces = DecimalPlaces.MONEY_DISPLAY,
                isNeededThousandSeparator = true
            )
        } else {
            globalAmountString
        }

        curTransaction = curTransaction.copy(amount = globalAmount)
        localAmount = newAmount

        writableState.update {
            it.copy(
                amountError = if (newAmount <= 0) {
                    TransactionAmountError.IncorrectInput
                } else {
                    TransactionAmountError.None
                },
                amountInLocalCurrency = localAmountString,
                amountInGlobalCurrency = globalAmountString,
                isUsedCurrencyExchange = it.localCurrency != it.globalCurrency,
            )
        }
    }

    private fun onCalculatorDismiss() {
        writableState.update { it.copy(isCalculatorOpen = false) }
    }

    private fun onCurrencyClick() {
        writableState.update { it.copy(isCurrencyMenuOpened = true) }
    }

    private fun onCurrencyChanged(newCurrency: Currency) {
        if (newCurrency.id != state.value.localCurrency.id) {
            val newGlobalAmount = test.getAmountAfterCurrencyExchanged(
                localAmount = localAmount,
                localCurrency = newCurrency,
                targetCurrency = state.value.globalCurrency
            )
            curTransaction = curTransaction.copy(amount = newGlobalAmount)

            writableState.update {
                it.copy(
                    amountInGlobalCurrency = numbersFormatter.toString(
                        number = newGlobalAmount,
                        decimalPlaces = DecimalPlaces.MONEY_DISPLAY,
                        isNeededThousandSeparator = true
                    ),
                    isUsedCurrencyExchange = newCurrency.id != it.globalCurrency.id,
                    isCurrencyMenuOpened = false,
                    localCurrency = newCurrency
                )
            }
        } else {
            onCurrencyDismiss()
        }
    }

    private fun onCurrencyDismiss() {
        writableState.update { it.copy(isCurrencyMenuOpened = false) }
    }

    private fun onNoteChanged(newNote: String) {
        if (newNote != state.value.note) {
            curTransaction = curTransaction.copy(note = newNote)
            writableState.update { it.copy(note = newNote) }
        }
    }

    private fun onCategoriesBottomSheetOpened() {
        writableState.update { it.copy(isCategoriesBottomSheetOpened = true) }
    }

    private fun onCategoriesBottomSheetDismiss() {
        writableState.update { it.copy(isCategoriesBottomSheetOpened = false) }
    }

    private fun onCategoryClick(newCategoryId: Long) {
        if (newCategoryId != state.value.selectedCategory?.id) {
            val newCategoryUi = state.value.categories.find { it.id == newCategoryId }
            val newCategory = if (newCategoryUi != null) {
                categoryUiConverter.map(category = newCategoryUi)
            } else {
                getEmptyCategory(state.value.selectedCategoryType)
            }
            curTransaction = curTransaction.copy(category = newCategory)
            selectedCategories[state.value.selectedCategoryType.ordinal] = newCategory.id

            writableState.update {
                it.copy(
                    selectedCategory = categoryUiConverter.map(newCategory),
                    isCategoryError = curTransaction.category.id == ZERO_ID,
                    isCategoriesBottomSheetOpened = false
                )
            }
        } else {
            onCategoriesBottomSheetDismiss()
        }
    }

    private fun onNavigateToCategoryScreen() {
        writableAction.tryEmit(TransactionAction.NavigateToCategoryScreen)
    }

    private fun onSaveClick() {
        val isCategoryError = curTransaction.category.id == ZERO_ID

        val amountError = if (curTransaction.amount <= 0) {
            TransactionAmountError.IncorrectInput
        } else {
            state.value.amountError
        }

        if (isCategoryError || state.value.dateError != TransactionDateError.None
            || amountError != TransactionAmountError.None
        ) {
            writableState.update {
                it.copy(
                    isCategoryError = isCategoryError,
                    isSaveTransactionErrorDialogOpened = true,
                    amountError = amountError
                )
            }
        } else {
            if (transactionUiConverter.map(curTransaction, state.value.globalCurrency)
                != initialTransactionUi
            ) {
                if (state.value.isCreateTransactionScreen) {
                    test.createTransaction(curTransaction)
                } else {
                    test.updateTransaction(curTransaction)
                }
            }
            onNavigateBack()
        }
    }

    private fun onSaveDialogDismiss() {
        writableState.update { it.copy(isSaveTransactionErrorDialogOpened = false) }
    }

    private fun onDeleteClick() {
        writableState.update { it.copy(isTransactionDeleteDialogOpened = true) }
    }

    private fun onDeleteDialogConfirm() {
        test.deleteTransaction(curTransaction)

        writableState.update { it.copy(isTransactionDeleteDialogOpened = false) }
        onNavigateBack()
    }

    private fun onDeleteDialogDismiss() {
        writableState.update { it.copy(isTransactionDeleteDialogOpened = false) }
    }

    private fun getInitialTransaction(
        transactionId: Long?,
        categories: List<Category>,
        filterId: Long?,
    ): Transaction {
        val defaultCategory: Category = if (categories.isNotEmpty()) {
            categories.first()
        } else {
            getEmptyCategory(state.value.selectedCategoryType)
        }

        val filter: Category = if (categories.isNotEmpty() && filterId != null) {
            categories.find { it.id == filterId } ?: defaultCategory
        } else {
            defaultCategory
        }

        return if (transactionId != null) {
            test.getTransaction(transactionId) ?: getEmptyTransaction(defaultCategory)
        } else {
            getEmptyTransaction(category = filter)
        }
    }

    private fun getEmptyTransaction(category: Category): Transaction {
        return Transaction(
            id = ZERO_ID,
            category = category,
            dateMillis = dateFormatter.dateToMillis(date = LocalDate.now()),
            amount = ZERO_AMOUNT
        )
    }

    private fun getEmptyCategory(categoryType: CategoryType): Category {
        return Category(
            id = ZERO_ID,
            name = "",
            type = categoryType,
            icon = CategoryIcon(
                icon = CategoryIcons.IC_SHOP_CART,
                color = CategoryColors.ORANGE_T70
            )
        )
    }

    private companion object {
        const val ZERO_ID = 0L
        const val ZERO_AMOUNT = 0.0
    }
}
