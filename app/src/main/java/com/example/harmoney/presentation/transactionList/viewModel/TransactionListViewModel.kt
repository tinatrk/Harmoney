package com.example.harmoney.presentation.transactionList.viewModel

import androidx.lifecycle.ViewModel
import com.example.harmoney.presentation.transactionList.models.TransactionListAction
import com.example.harmoney.presentation.transactionList.models.TransactionListState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class TransactionListViewModel(categoryId: Long?) : ViewModel() {
    private val _screenState = MutableStateFlow(TransactionListState())
    val screenState: StateFlow<TransactionListState> = _screenState.asStateFlow()

    private val _action = MutableSharedFlow<TransactionListAction?>(
        replay = 0,
        extraBufferCapacity = 1
    )
    val action: SharedFlow<TransactionListAction?> = _action.asSharedFlow()


    init {
        _screenState.value = TransactionListState(categoryId = categoryId)
    }

    fun onCreateTransaction(categoryId: Long?) {
        _action.tryEmit(TransactionListAction.NavigateToCreatingTransaction(categoryId))
    }

    fun onOpenTransaction(transactionId: Long?) {
        _action.tryEmit(TransactionListAction.NavigateToOpeningTransaction(transactionId))
    }

    fun onNavigateBack() {
        _action.tryEmit(TransactionListAction.NavigateBack)
    }
}
