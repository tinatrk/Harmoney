package com.example.harmoney.presentation.transaction.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.harmoney.navigation.NavResultKeys
import com.example.harmoney.presentation.transaction.models.TransactionAction
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
    categoryId: Long?, transactionId: Long?,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _screenState = MutableStateFlow(TransactionState())
    val screenState: StateFlow<TransactionState> = _screenState.asStateFlow()

    private val navCategory: StateFlow<Long?> =
        savedStateHandle.getStateFlow(NavResultKeys.SELECTED_CATEGORY, null)

    private val _action = MutableSharedFlow<TransactionAction?>(
        replay = 0,
        extraBufferCapacity = 1
    )
    val action: SharedFlow<TransactionAction?> = _action.asSharedFlow()

    init {
        _screenState.value =
            TransactionState(
                categoryId = categoryId,
                transactionId = transactionId
            )

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

    fun onNavigateToCategoryList(categoryTypeId: Long?) {
        _action.tryEmit(
            TransactionAction
                .NavigateToCategoryListScreen(categoryTypeId = categoryTypeId)
        )
    }

    fun onNavigateBack() {
        //clearSavedState()
        _action.tryEmit(TransactionAction.NavigateBack)
    }

    // вызвать перед выходом с экрана
    fun clearSavedState() {
        savedStateHandle[NavResultKeys.SELECTED_CATEGORY] = null
    }
}
