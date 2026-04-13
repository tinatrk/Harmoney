package com.example.harmoney.presentation.category.viewModel

import androidx.lifecycle.ViewModel
import com.example.harmoney.presentation.category.models.CategoryAction
import com.example.harmoney.presentation.category.models.CategoryState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class CategoryViewModel(categoryId: Long?, categoryTypeId: Long?) : ViewModel() {
    private val _screenState = MutableStateFlow(CategoryState())
    val screenState: StateFlow<CategoryState> = _screenState.asStateFlow()

    private val _action = MutableSharedFlow<CategoryAction?>(
        replay = 0,
        extraBufferCapacity = 1
    )
    val action: SharedFlow<CategoryAction?> = _action.asSharedFlow()


    init {
        _screenState.value = CategoryState(categoryId = categoryId, categoryTypeId = categoryTypeId)
    }

    fun onNavigateBack() {
        _action.tryEmit(CategoryAction.NavigateBack)
    }
}
