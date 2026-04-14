package com.example.harmoney.presentation.categoryList.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.navigation.NavResultKeys
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
    categoryTypeId: Long?,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _screenState = MutableStateFlow(CategoryListState())
    val screenState: StateFlow<CategoryListState> = _screenState.asStateFlow()

    private val _action = MutableSharedFlow<CategoryListAction?>(
        replay = 0,
        extraBufferCapacity = 1
    )
    val action: SharedFlow<CategoryListAction?> = _action.asSharedFlow()

    init {
        val categoryType =
            categoryTypeId?.let { CategoryType.fromId(categoryTypeId) } ?: CategoryType.Expenses
        _screenState.update {
            it.copy(
                categoryType = categoryType,
                selectedTabIndex = categoryType.ordinal,
                categoryInfo = getCategoryInfo(categoryType),
                isChoiceCategoryScreen = categoryTypeId != null
            )
        }
    }

    fun obtainEvent(event: CategoryListEvent) {
        when (event) {
            is CategoryListEvent.OnBackClick -> onNavigateBack()
            is CategoryListEvent.OnTabClick -> onTabClick(event.categoryType)
            is CategoryListEvent.OnCategoryClick -> {
                if (_screenState.value.isChoiceCategoryScreen) {
                    onChoiceCategory(event.categoryId)
                } else {
                    onOpenCategory(event.categoryId)
                }
            }

            is CategoryListEvent.OnFloatingButtonClick -> onCreateCategory()
        }
    }

    private fun onTabClick(newCategoryType: CategoryType) {
        if (_screenState.value.categoryType.id != newCategoryType.id) {
            _screenState.update {
                it.copy(
                    categoryType = newCategoryType,
                    selectedTabIndex = newCategoryType.ordinal,
                    categoryInfo = getCategoryInfo(newCategoryType)
                )
            }
        }
    }

    private fun onCreateCategory() {
        _action.tryEmit(
            CategoryListAction
                .NavigateToCreatingCategory(_screenState.value.categoryType.id)
        )
    }

    private fun onOpenCategory(categoryId: Long?) {
        _action.tryEmit(CategoryListAction.NavigateToOpeningCategory(categoryId))
    }

    private fun onNavigateBack() {
        onReturnWithoutParam()
    }

    private fun onChoiceCategory(categoryId: Long) {
        onReturnWithParam(categoryId)
    }

    private fun onReturnWithParam(param: Long) {
        savedStateHandle[NavResultKeys.SELECTED_CATEGORY] = param
        _action.tryEmit(CategoryListAction.NavigateBack)
    }

    private fun onReturnWithoutParam() {
        savedStateHandle[NavResultKeys.SELECTED_CATEGORY] = null
        _action.tryEmit(CategoryListAction.NavigateBack)
    }

    private fun getCategoryInfo(categoryType: CategoryType): String {
        return when (categoryType) {
            CategoryType.Expenses -> "Информация по расходам"
            CategoryType.Income -> "Информация по доходам"
        }
    }
}
