package com.example.harmoney.presentation.categoryList.viewModel

import androidx.lifecycle.ViewModel
import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.presentation.categoryList.models.CategoryListAction
import com.example.harmoney.presentation.categoryList.models.CategoryListEvent
import com.example.harmoney.presentation.categoryList.models.CategoryListState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CategoryListViewModel(
    categoryType: CategoryType,
) : ViewModel() {
    private val _screenState = MutableStateFlow(
        CategoryListState(
            selectedCategoryType = categoryType,
            selectedTabIndex = categoryType.ordinal,
        )
    )
    val screenState: StateFlow<CategoryListState> = _screenState.asStateFlow()

    private val _action = MutableSharedFlow<CategoryListAction?>(
        replay = 0,
        extraBufferCapacity = 1
    )
    val action: SharedFlow<CategoryListAction?> = _action.asSharedFlow()

    init {
        _screenState.update {
            it.copy(
                selectedCategoryType = categoryType,
                selectedTabIndex = categoryType.ordinal,
                categoryInfo = getCategoryInfo(categoryType),
            )
        }
    }

    fun obtainEvent(event: CategoryListEvent) {
        when (event) {
            is CategoryListEvent.OnBackClick -> onNavigateBack()
            is CategoryListEvent.OnTabClick -> onTabClick(event.categoryType)
            is CategoryListEvent.OnCategoryClick -> {
                onOpenCategory(event.categoryId)
            }

            is CategoryListEvent.OnFloatingButtonClick -> onCreateCategory()
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

    private fun onCreateCategory() {
        _action.tryEmit(
            CategoryListAction
                .NavigateToCreatingCategory(_screenState.value.selectedCategoryType.id)
        )
    }

    private fun onOpenCategory(categoryId: Long?) {
        _action.tryEmit(CategoryListAction.NavigateToOpeningCategory(categoryId))
    }

    private fun onNavigateBack() {
        _action.tryEmit(CategoryListAction.NavigateBack)
    }

    private fun getCategoryInfo(categoryType: CategoryType): String {
        return when (categoryType) {
            CategoryType.EXPENSES -> "Информация по расходам"
            CategoryType.INCOME -> "Информация по доходам"
        }
    }
}
