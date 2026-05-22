package com.example.harmoney.presentation.transaction.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.navigation.NavResultKeys
import com.example.harmoney.presentation.transaction.models.TransactionAction
import com.example.harmoney.presentation.transaction.models.TransactionEvent
import com.example.harmoney.presentation.transaction.models.TransactionState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@Suppress("detekt:LongParameterList", "detekt:TooManyFunctions",
    "detekt:CyclomaticComplexMethod")
class TransactionViewModel(
    categoryType: CategoryType,
    categoryId: Long?,
    transactionId: Long?,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _screenState = MutableStateFlow(
        TransactionState(
            selectedCategoryId = categoryId ?: ZERO_ID,
            isCreateTransactionScreen = transactionId == null,
            selectedCategoryType = categoryType,
            selectedTabIndex = categoryType.ordinal,
        )
    )
    val screenState: StateFlow<TransactionState> = _screenState.asStateFlow()

    private val navCategory: StateFlow<Long?> =
        savedStateHandle.getStateFlow(NavResultKeys.SELECTED_CATEGORY, null)

    private val _action = MutableSharedFlow<TransactionAction?>(
        replay = 0,
        extraBufferCapacity = 1
    )
    val action: SharedFlow<TransactionAction?> = _action.asSharedFlow()

    init {
        _screenState.update {
            it.copy(
                isCreateTransactionScreen = transactionId == null
            )
        }

        viewModelScope.launch {
            navCategory.collect { returnedCategoryId ->
                if (returnedCategoryId != null && returnedCategoryId != categoryId) {
                    _screenState.update { state ->
                        state.copy(selectedCategoryId = returnedCategoryId)
                    }
                }
            }
        }
    }

    fun obtainEvent(event: TransactionEvent) {
        when (event) {
            is TransactionEvent.OnBackClick -> onNavigateBack()
            is TransactionEvent.OnBackDialogConfirm -> {}
            is TransactionEvent.OnBackDialogDismiss -> {}

            is TransactionEvent.OnTabClick -> onTabClick(event.categoryType)

            is TransactionEvent.OnDateDialogOpen -> {}
            is TransactionEvent.OnDateDialogConfirm -> {}
            is TransactionEvent.OnDateDialogDismiss -> {}
            is TransactionEvent.OnDateErrorDialogDismiss -> {}

            is TransactionEvent.OnCalculatorOpen -> {}
            is TransactionEvent.OnAmountChanged -> {}
            is TransactionEvent.OnCalculatorDismiss -> {}
            is TransactionEvent.OnCurrencyClick -> {}
            is TransactionEvent.OnCurrencyChanged -> {}
            is TransactionEvent.OnCurrencyDismiss -> {}

            is TransactionEvent.OnNoteChanged -> {}

            is TransactionEvent.OnCategoryClick -> {}
            is TransactionEvent.OnMoreCategoriesClick -> onNavigateToCategoryList()

            is TransactionEvent.OnSaveClick -> onNavigateBack()
            is TransactionEvent.OnSaveDialogDismiss -> {}
            is TransactionEvent.OnCloseScreen -> clearSavedState()
        }
    }

    private fun onTabClick(newCategoryType: CategoryType) {
        if (_screenState.value.selectedCategoryType.id != newCategoryType.id) {
            _screenState.update {
                it.copy(
                    selectedCategoryType = newCategoryType,
                    selectedTabIndex = newCategoryType.ordinal,
                )
            }
        }
    }

    private fun onNavigateToCategoryList() {
        _action.tryEmit(
            TransactionAction.NavigateToCategoryListScreen
        )
    }

    private fun onNavigateBack() {
        _action.tryEmit(TransactionAction.NavigateBack)
    }

    // вызвать перед выходом с экрана
    private fun clearSavedState() {
        savedStateHandle[NavResultKeys.SELECTED_CATEGORY] = null
    }

    private companion object {
        const val ZERO_ID = 0L
    }
}
