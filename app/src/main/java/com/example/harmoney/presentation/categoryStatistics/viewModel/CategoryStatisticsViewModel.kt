package com.example.harmoney.presentation.categoryStatistics.viewModel

import androidx.lifecycle.ViewModel
import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.presentation.categoryStatistics.models.CategoryStatisticsAction
import com.example.harmoney.presentation.categoryStatistics.models.CategoryStatisticsEvent
import com.example.harmoney.presentation.categoryStatistics.models.CategoryStatisticsState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CategoryStatisticsViewModel() : ViewModel() {
    private val _screenState = MutableStateFlow(CategoryStatisticsState())
    val screenState: StateFlow<CategoryStatisticsState> = _screenState.asStateFlow()

    private val _action = MutableSharedFlow<CategoryStatisticsAction?>(
        replay = 0,
        extraBufferCapacity = 1
    )
    val action: SharedFlow<CategoryStatisticsAction?> = _action.asSharedFlow()

    init {
        // считать тему из shared preferences
        _screenState.update {
            it.copy(
                categoryInfo = getCategoryInfo(_screenState.value.categoryType)
            )
        }
    }

    fun obtainEvent(event: CategoryStatisticsEvent) {
        when (event) {
            is CategoryStatisticsEvent.OnTabClick -> onTabClick(event.categoryType)
            is CategoryStatisticsEvent.OnSettingsIconClick -> onNavigateToSettings()
            is CategoryStatisticsEvent.OnTransactionListIconClick -> onNavigateToTransactionList(
                null
            )

            is CategoryStatisticsEvent.OnFloatingButtonClick -> onNavigateToTransaction()
            is CategoryStatisticsEvent.OnCategoryClick -> onNavigateToTransactionList(
                event.categoryId
            )

            is CategoryStatisticsEvent.OnChangeTheme -> {
                // в будущем поменять логику на shared preferences
                _screenState.update {
                    it.copy(
                        isThemeDark = !_screenState.value.isThemeDark
                    )
                }
            }

            is CategoryStatisticsEvent.OnFirstDayMonthClick -> {/* работа с диалоговым окном */
            }

            is CategoryStatisticsEvent.OnCategoryListClick -> {
                onNavigateToCategoryList()
            }
        }
    }

    private fun onNavigateToTransactionList(categoryId: Long?) {
        _action.tryEmit(CategoryStatisticsAction.NavigateToTransactionList(categoryId))
    }

    private fun onNavigateToTransaction() {
        _action.tryEmit(CategoryStatisticsAction.NavigateToTransaction)
    }

    private fun onNavigateToSettings() {
        _action.tryEmit(CategoryStatisticsAction.NavigateToSettings)
    }

    private fun onNavigateToCategoryList() {
        _action.tryEmit(CategoryStatisticsAction.NavigateToCategoryList)
    }

    private fun onTabClick(newCategoryType: CategoryType) {
        if (_screenState.value.categoryType.id != newCategoryType.id) {
            _screenState.update {
                it.copy(
                    categoryType = newCategoryType,
                    categoryInfo = getCategoryInfo(newCategoryType),
                    selectedTabIndex = newCategoryType.ordinal
                )
            }
        }
    }

    private fun getCategoryInfo(categoryType: CategoryType): String {
        return when (categoryType) {
            CategoryType.Expenses -> "Информация по расходам"
            CategoryType.Income -> "Информация по доходам"
        }
    }
}
