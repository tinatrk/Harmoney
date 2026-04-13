package com.example.harmoney.presentation.categoryStatistics.viewModel

import androidx.lifecycle.ViewModel
import com.example.harmoney.presentation.categoryStatistics.models.CategoryStatisticsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CategoryStatisticsViewModel() : ViewModel() {
    private val _screenState = MutableStateFlow(CategoryStatisticsState())
    val screenState: StateFlow<CategoryStatisticsState> = _screenState.asStateFlow()

    init {
        _screenState.value = CategoryStatisticsState(currentBalance = "")
    }
}
