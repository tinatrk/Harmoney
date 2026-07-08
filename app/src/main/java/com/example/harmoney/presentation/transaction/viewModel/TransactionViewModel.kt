package com.example.harmoney.presentation.transaction.viewModel

import androidx.lifecycle.viewModelScope
import com.example.harmoney.base.BaseViewModel
import com.example.harmoney.core.session.SessionStateHolder
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
import com.example.harmoney.presentation.models.DecimalPlaces
import com.example.harmoney.presentation.test.TestDataSource
import com.example.harmoney.presentation.transaction.models.EditableTransaction
import com.example.harmoney.presentation.transaction.models.TransactionAction
import com.example.harmoney.presentation.transaction.models.TransactionAmountError
import com.example.harmoney.presentation.transaction.models.TransactionDateError
import com.example.harmoney.presentation.transaction.models.TransactionEvent
import com.example.harmoney.presentation.transaction.models.TransactionState
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import java.time.LocalDate

@Suppress("detekt:LongParameterList", "detekt:TooManyFunctions")
class TransactionViewModel(
    categoryId: Long?,
    transactionId: Long?,
    private val sessionSateHolder: SessionStateHolder,
    private val test: TestDataSource,
    private val numbersFormatter: NumbersFormatter,
    private val dateFormatter: DateFormatter,
    private val transactionUiConverter: TransactionUiConverter,
    private val categoryUiConverter: CategoryUiConverter
) : BaseViewModel<TransactionEvent, TransactionAction, TransactionState>(
    state =
        TransactionState(
            isCreateTransactionScreen = transactionId == null,
        )
) {
    override val tag: String = TransactionViewModel::class.java.simpleName ?: ""

    private val initialTransaction: Transaction
    private var editableTransaction: EditableTransaction
    private var amountInLocalCurrency: Double

    private val selectedCategories: MutableList<Long> =
        CategoryType.entries.map { ZERO_ID }.toMutableList()

    private val firstDay = test.getFirstDay()
    private val lastDay = test.getLastDay()

    init {
        // TODO() в будущем считывать валюту из sharedPreferences
        initialTransaction = test.getTransaction(transactionId) ?: getEmptyTransaction()
        editableTransaction = EditableTransaction(
            amount = initialTransaction.amount,
            date = initialTransaction.date,
            note = initialTransaction.note,
            categoryId = initialTransaction.category.id,
            categoryType = initialTransaction.category.type
        )
        selectedCategories[editableTransaction.categoryType.ordinal] =
            editableTransaction.categoryId
        val amountString = numbersFormatter.toString(
            number = editableTransaction.amount,
            decimalPlaces = DecimalPlaces.MONEY_DISPLAY,
            isNeededThousandSeparator = true
        )
        amountInLocalCurrency = editableTransaction.amount

        sessionSateHolder.setCategoryType(editableTransaction.categoryType)


        writableState.update {
            it.copy(
                selectedDate = dateFormatter.formatDate(date = editableTransaction.date),
                localCurrency = it.globalCurrency,
                amountInLocalCurrency = amountString,
                amountInGlobalCurrency = amountString,
                amountError = TransactionAmountError.None,
                note = editableTransaction.note,
            )
        }

        sessionSateHolder.state.map { it.categoryType }
            .distinctUntilChanged()
            .flatMapLatest { categoryType ->
                flow {
                    emit(categoryType to test.getCategories(categoryType))
                }
            }.onEach { (categoryType, categories) ->

                val oldCategoryId = selectedCategories[categoryType.ordinal]
                val newCategoryId = when {
                    categories.isEmpty() -> null
                    oldCategoryId == ZERO_ID -> categories.first().id
                    oldCategoryId != ZERO_ID -> categories.find { it.id == oldCategoryId }?.id
                    else -> null
                }

                val curCategory = categories.find { it.id == newCategoryId }
                editableTransaction = editableTransaction.copy(
                    categoryId =
                        curCategory?.id ?: ZERO_ID, categoryType = categoryType
                )

                writableState.update {
                    it.copy(
                        selectedCategoryType = categoryType,
                        selectedTabIndex = categoryType.ordinal,
                        categories = categoryUiConverter.map(categories),
                        selectedCategory = curCategory?.let { categoryUiConverter.map(curCategory) }
                    )
                }
            }.launchIn(viewModelScope)
    }

    @Suppress("detekt:CyclomaticComplexMethod")
    override fun obtainEvent(event: TransactionEvent) {
        when (event) {
            is TransactionEvent.OnBackClick -> onBackClick()
            is TransactionEvent.OnBackDialogConfirm -> onBackDialogConfirm()
            is TransactionEvent.OnBackDialogDismiss -> onBackDialogDismiss()

            is TransactionEvent.OnTabClick -> onTabClick(event.categoryType)

            is TransactionEvent.OnDateDialogOpen -> onDateDialogOpen()
            is TransactionEvent.OnDateDialogConfirm -> onDateDialogConfirm(event.newDate)
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
        val isEdited = editableTransaction.amount != initialTransaction.amount
                || editableTransaction.date != initialTransaction.date
                || editableTransaction.note != initialTransaction.note
                || editableTransaction.categoryId != initialTransaction.category.id
        if (isEdited) {
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
            sessionSateHolder.setCategoryType(newCategoryType)
        }
    }

    private fun onDateDialogOpen() {
        writableState.update { it.copy(isDatePickerOpened = true) }
    }

    private fun onDateDialogConfirm(newDate: LocalDate?) {

        val date = newDate ?: LocalDate.now()
        val isDateCorrect = date in firstDay..lastDay

        editableTransaction = editableTransaction.copy(date = date)

        writableState.update {
            it.copy(
                dateError = if (isDateCorrect) {
                    TransactionDateError.None
                } else {
                    TransactionDateError.OutOfRange(
                        firstDay = dateFormatter.formatDate(date = firstDay),
                        lastDay = dateFormatter.formatDate(date = lastDay),
                    )
                },
                isDatePickerOpened = !isDateCorrect,
                selectedDate = dateFormatter.formatDate(date = date),
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
        if (editableTransaction.amount == newAmount) return

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

        editableTransaction = editableTransaction.copy(amount = globalAmount)
        amountInLocalCurrency = newAmount

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
                localAmount = amountInLocalCurrency,
                localCurrency = newCurrency,
                targetCurrency = state.value.globalCurrency
            )
            editableTransaction = editableTransaction.copy(amount = newGlobalAmount)

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
            editableTransaction = editableTransaction.copy(note = newNote)
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
            editableTransaction = editableTransaction.copy(
                categoryId = newCategory.id,
                categoryType = newCategory.type
            )
            selectedCategories[state.value.selectedCategoryType.ordinal] = newCategory.id

            writableState.update {
                it.copy(
                    selectedCategory = categoryUiConverter.map(newCategory),
                    isCategoryError = editableTransaction.categoryId == ZERO_ID,
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
        val isCategoryError =
            editableTransaction.categoryId == ZERO_ID

        val amountError = if (editableTransaction.amount < 0) {
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
            val isEdited = editableTransaction.amount != initialTransaction.amount
                    || editableTransaction.date != initialTransaction.date
                    || editableTransaction.note != initialTransaction.note
                    || editableTransaction.categoryId != initialTransaction.category.id
            if (isEdited) {
                if (state.value.isCreateTransactionScreen) {
                    //достать категорию и создать транзакцию
                    //test.createTransaction(curTransaction)
                } else {
                    //test.updateTransaction(curTransaction)
                    //достать категорию и обновить транзакцию
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
        //достать категорию и удалить транзакцию
        //test.deleteTransaction(curTransaction)

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
            test.getTransaction(transactionId) ?: getEmptyTransaction()//(defaultCategory)
        } else {
            getEmptyTransaction()//(category = filter)
        }
    }

    private fun getEmptyTransaction(): Transaction {
        return Transaction(
            id = ZERO_ID,
            category = getEmptyCategory(CategoryType.EXPENSES),
            date = LocalDate.now(),
            amount = ZERO_AMOUNT,
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
