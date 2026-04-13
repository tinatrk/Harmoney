package com.example.harmoney.presentation.categoryList.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.harmoney.navigation.NavResultKeys
import com.example.harmoney.presentation.categoryList.models.CategoryListAction
import com.example.harmoney.presentation.categoryList.models.CategoryListState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

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
        _screenState.value = CategoryListState(categoryTypeId = categoryTypeId)
    }

    fun onCreateCategory(categoryTypeId: Long?) {
        _action.tryEmit(CategoryListAction.NavigateToCreatingCategory(categoryTypeId))
    }

    fun onOpenCategory(categoryId: Long?) {
        _action.tryEmit(CategoryListAction.NavigateToOpeningCategory(categoryId))
    }

    fun onNavigateBack() {
        onReturnWithoutParam()
    }

    fun onCategoryClick(categoryId: Long) {
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
}
