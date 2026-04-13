package com.example.harmoney.presentation.transactionList.viewModel

import androidx.lifecycle.ViewModel
import com.example.harmoney.presentation.transactionList.models.TransactionListState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TransactionListViewModel(categoryId: Long?) : ViewModel() {
    private val _screenState = MutableStateFlow(TransactionListState())
    val screenState: StateFlow<TransactionListState> = _screenState.asStateFlow()

    init {
        _screenState.value = TransactionListState(categoryId = categoryId)
    }
}
