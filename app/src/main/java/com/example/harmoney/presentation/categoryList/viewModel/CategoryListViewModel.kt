package com.example.harmoney.presentation.categoryList.viewModel

import androidx.lifecycle.ViewModel
import com.example.harmoney.presentation.categoryList.models.CategoryListState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CategoryListViewModel(categoryTypeId: Long?) : ViewModel() {
    private val _screenState = MutableStateFlow(CategoryListState())
    val screenState: StateFlow<CategoryListState> = _screenState.asStateFlow()

    init {
        _screenState.value = CategoryListState(categoryTypeId = categoryTypeId)
    }
}
