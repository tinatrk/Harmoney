package com.example.harmoney.presentation.transaction.viewModel

import androidx.lifecycle.ViewModel
import com.example.harmoney.presentation.transaction.models.TransactionState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TransactionViewModel(categoryId: Long?, transactionId: Long?) : ViewModel() {
    private val _screenState = MutableStateFlow(TransactionState())
    val screenState: StateFlow<TransactionState> = _screenState.asStateFlow()

    init {
        _screenState.value =
            TransactionState(categoryId = categoryId, transactionId = transactionId)
    }
}
