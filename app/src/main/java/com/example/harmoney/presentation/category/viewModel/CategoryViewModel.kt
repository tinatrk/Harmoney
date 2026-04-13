package com.example.harmoney.presentation.category.viewModel

import androidx.lifecycle.ViewModel
import com.example.harmoney.presentation.category.models.CategoryState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CategoryViewModel(categoryId: Long?, categoryTypeId: Long?) : ViewModel() {
    private val _screenState = MutableStateFlow(CategoryState())
    val screenState: StateFlow<CategoryState> = _screenState.asStateFlow()

    init {
        _screenState.value = CategoryState(categoryId = categoryId, categoryTypeId = categoryTypeId)
    }
}
