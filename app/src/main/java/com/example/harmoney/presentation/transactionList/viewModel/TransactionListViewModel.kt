package com.example.harmoney.presentation.transactionList.viewModel

import androidx.lifecycle.ViewModel
import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.presentation.transactionList.models.TransactionListAction
import com.example.harmoney.presentation.transactionList.models.TransactionListEvent
import com.example.harmoney.presentation.transactionList.models.TransactionListState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TransactionListViewModel(categoryId: Long?) : ViewModel() {
    private val _screenState = MutableStateFlow(TransactionListState())
    val screenState: StateFlow<TransactionListState> = _screenState.asStateFlow()

    private val _action = MutableSharedFlow<TransactionListAction?>(
        replay = 0,
        extraBufferCapacity = 1
    )
    val action: SharedFlow<TransactionListAction?> = _action.asSharedFlow()


    init {
        _screenState.update {
            it.copy(
                categoryId = categoryId,
            )
        }
    }

    fun obtainEvent(event: TransactionListEvent) {
        when (event) {
            is TransactionListEvent.OnBackClick -> onNavigateBack()
            is TransactionListEvent.OnTabClick -> onTabClick(event.categoryType)
            is TransactionListEvent.OnStatisticsPeriodClick -> {
                onStatisticPeriodClick(event.newPeriodId)
            }
            is TransactionListEvent.OnFloatingButtonClick -> onCreateTransaction(event.categoryId)
            is TransactionListEvent.OnTransactionClick -> onOpenTransaction(event.transactionId)
        }
    }

    private fun onNavigateBack() {
        _action.tryEmit(TransactionListAction.NavigateBack)
    }

    private fun onTabClick(newCategoryType: CategoryType) {
        if (_screenState.value.categoryType.id != newCategoryType.id) {
            _screenState.update {
                it.copy(
                    categoryType = newCategoryType,
                    selectedTabIndex = newCategoryType.ordinal,
                )
            }
        }
    }

    private fun onStatisticPeriodClick(newPeriodId: Long) {

    }

    private fun onCreateTransaction(categoryId: Long?) {
        _action.tryEmit(TransactionListAction.NavigateToCreatingTransaction(categoryId))
    }

    private fun onOpenTransaction(transactionId: Long?) {
        _action.tryEmit(TransactionListAction.NavigateToOpeningTransaction(transactionId))
    }
}
