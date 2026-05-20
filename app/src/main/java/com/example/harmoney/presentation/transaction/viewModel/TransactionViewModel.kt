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

class TransactionViewModel(
    categoryType: CategoryType,
    categoryId: Long?,
    transactionId: Long?,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _screenState = MutableStateFlow(
        TransactionState(
            categoryId = categoryId,
            transactionId = transactionId,
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
                categoryInfo = getCategoryInfo(_screenState.value.selectedCategoryType),
                isCreateTransactionScreen = transactionId == null
            )
        }

        viewModelScope.launch {
            navCategory.collect { returnedCategoryId ->
                if (returnedCategoryId != null && returnedCategoryId != categoryId) {
                    _screenState.update { state ->
                        state.copy(categoryId = returnedCategoryId)
                    }
                }
            }
        }
    }

    fun obtainEvent(event: TransactionEvent) {
        when (event) {
            is TransactionEvent.OnBackClick -> onNavigateBack()
            is TransactionEvent.OnTabClick -> onTabClick(event.categoryType)
            is TransactionEvent.OnMoreCategoriesClick -> onNavigateToCategoryList()
            is TransactionEvent.OnSaveClick -> onNavigateBack()
            is TransactionEvent.OnCloseScreen -> clearSavedState()
        }
    }

    private fun onTabClick(newCategoryType: CategoryType) {
        if (_screenState.value.selectedCategoryType.id != newCategoryType.id) {
            _screenState.update {
                it.copy(
                    selectedCategoryType = newCategoryType,
                    selectedTabIndex = newCategoryType.ordinal,
                    categoryInfo = getCategoryInfo(newCategoryType)
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

    private fun getCategoryInfo(categoryType: CategoryType): String {
        return when (categoryType) {
            CategoryType.Expenses -> "Информация по расходам"
            CategoryType.Income -> "Информация по доходам"
        }
    }
}
