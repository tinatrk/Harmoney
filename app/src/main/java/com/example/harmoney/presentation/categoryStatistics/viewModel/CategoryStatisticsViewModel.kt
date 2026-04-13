package com.example.harmoney.presentation.categoryStatistics.viewModel

import androidx.lifecycle.ViewModel
import com.example.harmoney.presentation.categoryStatistics.models.CategoryStatisticsAction
import com.example.harmoney.presentation.categoryStatistics.models.CategoryStatisticsState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class CategoryStatisticsViewModel() : ViewModel() {
    private val _screenState = MutableStateFlow(CategoryStatisticsState())
    val screenState: StateFlow<CategoryStatisticsState> = _screenState.asStateFlow()

    private val _action = MutableSharedFlow<CategoryStatisticsAction?>(
        replay = 0,
        extraBufferCapacity = 1
    )
    val action: SharedFlow<CategoryStatisticsAction?> = _action.asSharedFlow()

    init {
        _screenState.value = CategoryStatisticsState(currentBalance = "")
    }

    fun onNavigateToTransactionList(categoryId: Long?) {
        _action.tryEmit(CategoryStatisticsAction.NavigateToTransactionList(categoryId))
    }

    fun onNavigateToTransaction() {
        _action.tryEmit(CategoryStatisticsAction.NavigateToTransaction)
    }

    fun onNavigateToSettings() {
        _action.tryEmit(CategoryStatisticsAction.NavigateToSettings)
    }
}
