package com.example.harmoney.presentation.transaction.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.harmoney.base.BaseViewModel
import com.example.harmoney.domain.models.Category
import com.example.harmoney.domain.models.CategoryColors
import com.example.harmoney.domain.models.CategoryIcon
import com.example.harmoney.domain.models.CategoryIcons
import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.domain.models.Currency
import com.example.harmoney.domain.models.Transaction
import com.example.harmoney.navigation.NavResultKeys
import com.example.harmoney.presentation.converters.CategoryUiConverter
import com.example.harmoney.presentation.converters.DateFormatter
import com.example.harmoney.presentation.converters.NumbersFormatter
import com.example.harmoney.presentation.converters.TransactionUiConverter
import com.example.harmoney.presentation.models.TransactionUi
import com.example.harmoney.presentation.test.TestDataSource
import com.example.harmoney.presentation.transaction.models.TransactionAction
import com.example.harmoney.presentation.transaction.models.TransactionAmountError
import com.example.harmoney.presentation.transaction.models.TransactionDateError
import com.example.harmoney.presentation.transaction.models.TransactionEvent
import com.example.harmoney.presentation.transaction.models.TransactionState
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

@Suppress(
    "detekt:LongParameterList", "detekt:TooManyFunctions",
    "detekt:CyclomaticComplexMethod"
)
class TransactionViewModel(
    categoryType: CategoryType,
    categoryId: Long?,
    transactionId: Long?,
    private val savedStateHandle: SavedStateHandle,
    private val test: TestDataSource,
    private val numbersFormatter: NumbersFormatter,
    private val dateFormatter: DateFormatter,
    private val transactionUiConverter: TransactionUiConverter,
    private val categoryUiConverter: CategoryUiConverter
) : BaseViewModel<TransactionEvent, TransactionAction, TransactionState>(
    state =
        TransactionState(
            selectedCategoryId = categoryId ?: ZERO_ID,
            isCreateTransactionScreen = transactionId == null,
            selectedCategoryType = categoryType,
            selectedTabIndex = categoryType.ordinal,
        )
) {
    override val tag: String = TransactionViewModel::class.java.simpleName ?: ""

    private val navCategory: StateFlow<Long?> =
        savedStateHandle.getStateFlow(NavResultKeys.SELECTED_CATEGORY, null)

    private val startedTransactionUi: TransactionUi
    private var curTransaction: Transaction

    private val selectedCategories: MutableList<Long> =
        CategoryType.entries.map { ZERO_ID }.toMutableList()


    init {
        // TODO() в будущем считывать валюту из sharedPreferences

        val categories = test.getCategories(state.value.selectedCategoryType)

        curTransaction = getStartedTransaction(transactionId, categories)
        startedTransactionUi =
            transactionUiConverter.map(curTransaction, state.value.globalCurrency)

        selectedCategories[state.value.selectedCategoryType.ordinal] = curTransaction.category.id

        writableState.update {
            it.copy(
                isCreateTransactionScreen = startedTransactionUi.id == ZERO_ID,
                selectedDate = startedTransactionUi.date,
                //selectedDateMillis = startedTransaction.dateMillis,
                localCurrency = it.globalCurrency,
                amountInLocalCurrency = startedTransactionUi.amount,
                amountInGlobalCurrency = startedTransactionUi.amount,
                amountError = TransactionAmountError.None, // Empty при создании?
                categories = categoryUiConverter.map(categories),
                selectedCategoryId = startedTransactionUi.category.id
            )
        }

        viewModelScope.launch {
            navCategory.collect { returnedCategoryId ->
                if (returnedCategoryId != null && returnedCategoryId != categoryId) {
                    writableState.update { state ->
                        state.copy(selectedCategoryId = returnedCategoryId)
                    }
                }
            }
        }
    }

    override fun obtainEvent(event: TransactionEvent) {
        when (event) {
            is TransactionEvent.OnBackClick -> onBackClick()
            is TransactionEvent.OnBackDialogConfirm -> onBackDialogConfirm()
            is TransactionEvent.OnBackDialogDismiss -> onBackDialogDismiss()

            is TransactionEvent.OnTabClick -> onTabClick(event.categoryType)

            is TransactionEvent.OnDateDialogOpen -> onDateDialogOpen()
            is TransactionEvent.OnDateDialogConfirm -> onDateDialogConfirm(event.newDateMillis)
            is TransactionEvent.OnDateDialogDismiss -> onDateDialogDismiss()
            is TransactionEvent.OnDateErrorDialogDismiss -> onDateErrorDialogDismiss()

            is TransactionEvent.OnCalculatorOpen -> onCalculatorOpen()
            is TransactionEvent.OnAmountChanged -> onAmountChange(event.newAmount)
            is TransactionEvent.OnCalculatorDismiss -> onCalculatorDismiss()

            is TransactionEvent.OnCurrencyClick -> onCurrencyClick()
            is TransactionEvent.OnCurrencyChanged -> onCurrencyChanged(event.newCurrency)
            is TransactionEvent.OnCurrencyDismiss -> onCurrencyDismiss()

            is TransactionEvent.OnNoteChanged -> onNoteChanged(event.newNote)

            is TransactionEvent.OnCategoryClick -> onCategoryClick(event.categoryId)
            is TransactionEvent.OnMoreCategoriesClick -> onNavigateToCategoryList()

            is TransactionEvent.OnSaveClick -> onSaveClick()
            is TransactionEvent.OnSaveDialogDismiss -> onSaveDialogDismiss()
            is TransactionEvent.OnCloseScreen -> clearSavedState()
        }
    }

    private fun onBackClick() {
        val curTransactionUi = TransactionUi(
            id = startedTransactionUi.id,
            category = categoryUiConverter.map(
                test.getCategory(state.value.selectedCategoryId) ?: getEmptyCategory()
            ),
            date = state.value.selectedDate,
            amount = state.value.amountInGlobalCurrency,
            note = state.value.note,
            createdAt = startedTransactionUi.createdAt
        )

        if (startedTransactionUi != curTransactionUi) {
            writableState.update {
                it.copy(
                    isTransactionNotSavedDialogOpened = true
                )
            }
        } else {
            onNavigateBack()
        }
    }

    private fun onBackDialogConfirm() {
        writableState.update {
            it.copy(
                isTransactionNotSavedDialogOpened = false
            )
        }
        onNavigateBack()
    }

    private fun onBackDialogDismiss() {
        writableState.update {
            it.copy(
                isTransactionNotSavedDialogOpened = false
            )
        }
    }

    private fun onNavigateBack() {
        writableAction.tryEmit(TransactionAction.NavigateBack)
    }

    private fun onTabClick(newCategoryType: CategoryType) {
        if (state.value.selectedCategoryType.id != newCategoryType.id) {
            val categories = categoryUiConverter.map(test.getCategories(newCategoryType))
            val newCategoryId =
                if (selectedCategories[newCategoryType.ordinal] != ZERO_ID
                    && categories.isNotEmpty()
                ) {
                    categories.first().id
                } else {
                    null
                }
            curTransaction =
                curTransaction.copy(
                    category = curTransaction.category.copy(
                        id = newCategoryId ?: ZERO_ID, type = newCategoryType
                    )
                )

            writableState.update {
                it.copy(
                    selectedCategoryType = newCategoryType,
                    selectedTabIndex = newCategoryType.ordinal,
                    categories = categories,
                    selectedCategoryId = newCategoryId,
                    //isCategoryError = curTransaction.category.id == ZERO_ID //????
                )
            }
        }
    }

    private fun onDateDialogOpen() {
        writableState.update {
            it.copy(
                isDatePickerOpened = true
            )
        }
    }

    private fun onDateDialogConfirm(newDateMillis: Long?) {
        val isDateCorrect = test.isDateInCorrectRange(newDateMillis)
        if (isDateCorrect && newDateMillis != null) {
            curTransaction = curTransaction.copy(dateMillis = newDateMillis)

            writableState.update {
                it.copy(
                    dateError = TransactionDateError.None,
                    isDateErrorDialogOpen = false,
                    isDatePickerOpened = false,
                    selectedDate = dateFormatter.millisToString(
                        newDateMillis,
                        DATE_PATTERN
                    ), // Не очень нравится это решение
                )
            }
        } else {
            writableState.update {
                it.copy(
                    dateError = TransactionDateError.OutOfRange(
                        dateFormatter.millisToString(test.getFirstDay(), DATE_PATTERN),
                        dateFormatter.millisToString(test.getLastDay(), DATE_PATTERN),
                    ),
                    isDateErrorDialogOpen = true,
                    isDatePickerOpened = true,
                )
            }
        }
    }

    private fun onDateDialogDismiss() {
        writableState.update {
            it.copy(
                isDatePickerOpened = false,
            )
        }
    }

    private fun onDateErrorDialogDismiss() {
        writableState.update {
            it.copy(
                isDateErrorDialogOpen = false
            )
        }
    }

    private fun onCalculatorOpen() {
        writableState.update {
            it.copy(
                isCalculatorOpen = true
            )
        }
    }

    private fun onAmountChange(newAmount: Double) {
        if (newAmount <= 0) {
            writableState.update {
                it.copy(
                    amountError = TransactionAmountError.IncorrectInput
                )
            }
        } else {
            // преобразовать валюты, считав актуальный курс из интеренета по API
            val globalAmount = test.getAmountAfterCurrencyExchanged(
                localCurrency = state.value.localCurrency,
                targetCurrency = state.value.globalCurrency,
                localAmount = newAmount
            )

            curTransaction = curTransaction.copy(amount = globalAmount)

            writableState.update {
                it.copy(
                    amountError = TransactionAmountError.None,
                    amountInLocalCurrency = numbersFormatter.toString(
                        number = newAmount,
                        decimalPlaces = TWO_DECIMAL_PLACES,
                        isNeededThousandSeparator = true // false?????
                    ),
                    amountInGlobalCurrency = numbersFormatter.toString(
                        number = globalAmount,
                        decimalPlaces = TWO_DECIMAL_PLACES,
                        isNeededThousandSeparator = true // false?????
                    ),
                    isUsedCurrencyExchange = it.localCurrency != it.globalCurrency,
                )
            }
        }
    }

    private fun onCalculatorDismiss() {
        writableState.update {
            it.copy(
                isCalculatorOpen = false
            )
        }
    }

    private fun onCurrencyClick() {
        writableState.update {
            it.copy(
                isCurrencyMenuOpened = true
            )
        }
    }

    private fun onCurrencyChanged(newCurrency: Currency) {
        if (newCurrency.id != state.value.localCurrency.id) {
            val newGlobalAmount = test.getAmountAfterCurrencyExchanged(
                localAmount = curTransaction.amount,
                localCurrency = newCurrency,
                targetCurrency = state.value.globalCurrency
            )
            curTransaction = curTransaction.copy(amount = newGlobalAmount)

            writableState.update {
                it.copy(
                    amountInGlobalCurrency = numbersFormatter.toString(
                        number = newGlobalAmount,
                        decimalPlaces = TWO_DECIMAL_PLACES,
                        isNeededThousandSeparator = false
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
        writableState.update {
            it.copy(
                isCurrencyMenuOpened = false
            )
        }
    }

    private fun onNoteChanged(newNote: String) {
        if (newNote != state.value.note) {
            curTransaction = curTransaction.copy(note = newNote)
            writableState.update {
                it.copy(
                    note = newNote
                )
            }
        }
    }

    private fun onCategoryClick(newCategoryId: Long) {
        if (newCategoryId != state.value.selectedCategoryId) {
            val newCategoryUi =
                state.value.categories.find { it.id == newCategoryId }
            val newCategory = if (newCategoryUi != null) {
                categoryUiConverter.map(newCategoryUi)
            } else {
                getEmptyCategory()
            }
            curTransaction = curTransaction.copy(category = newCategory)

            writableState.update {
                it.copy(
                    selectedCategoryId = curTransaction.category.id,//newCategoryId,
                    isCategoryError = curTransaction.category.id == ZERO_ID
                )
            }
        }
    }

    private fun onNavigateToCategoryList() {
        writableAction.tryEmit(
            TransactionAction.NavigateToCategoryListScreen
        )
    }

    private fun onSaveClick() {
        if ((curTransaction.category.id == ZERO_ID)
            || state.value.dateError != TransactionDateError.None
            || state.value.amountError != TransactionAmountError.None
        ) {
            writableState.update {
                it.copy(
                    isCategoryError = curTransaction.category.id == ZERO_ID,
                    isSaveTransactionErrorDialogOpened = true
                )
            }
        } else {
            // TODO() сохранить транзакцию
            test.saveTransaction(curTransaction)
            onNavigateBack()
        }
    }

    private fun onSaveDialogDismiss() {
        writableState.update {
            it.copy(
                isSaveTransactionErrorDialogOpened = false
            )
        }
    }

    // вызвать перед выходом с экрана
    private fun clearSavedState() {
        savedStateHandle[NavResultKeys.SELECTED_CATEGORY] = null
    }

    private fun getStartedTransaction(
        transactionId: Long?,
        categories: List<Category>,
    ): Transaction {
        val defaultCategory: Category = if (categories.isNotEmpty()) {
            categories.first()
        } else {
            getEmptyCategory()
        }

        return if (transactionId != null) {
            test.getTransaction(transactionId) ?: getEmptyTransaction(defaultCategory)
        } else {
            getEmptyTransaction(defaultCategory)
        }
    }

    private fun getEmptyTransaction(category: Category): Transaction {
        return Transaction(
            id = ZERO_ID,
            category = category,
            dateMillis = dateFormatter.dateToMillis(LocalDate.now()),
            amount = ZERO_AMOUNT
        )
    }

    private fun getEmptyCategory(): Category {
        return Category(
            id = ZERO_ID,
            name = "",
            type = state.value.selectedCategoryType,
            icon = CategoryIcon(
                icon = CategoryIcons.IC_SHOP_CART,
                color = CategoryColors.ORANGE_T70
            )
        )
    }

    private companion object {
        const val ZERO_ID = 0L
        const val DATE_PATTERN = "dd MMMM"
        const val ZERO_AMOUNT = 0.0
        const val TWO_DECIMAL_PLACES = 2
    }
}
