package com.example.harmoney.presentation.category.viewModel

import androidx.lifecycle.ViewModel
import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.presentation.category.models.CategoryAction
import com.example.harmoney.presentation.category.models.CategoryEvent
import com.example.harmoney.presentation.category.models.CategoryState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CategoryViewModel(categoryType: CategoryType, categoryId: Long?) : ViewModel() {
    private val _screenState = MutableStateFlow(CategoryState(
        selectedCategoryType = categoryType,
        selectedTabIndex = categoryType.ordinal,))
    val screenState: StateFlow<CategoryState> = _screenState.asStateFlow()

    private val _action = MutableSharedFlow<CategoryAction?>(
        replay = 0,
        extraBufferCapacity = 1
    )
    val action: SharedFlow<CategoryAction?> = _action.asSharedFlow()

    init {
        _screenState.update {
            it.copy(
                categoryId = categoryId,
                selectedCategoryType = categoryType,
                isCreateCategoryScreen = categoryId == null
            )
        }
    }

    fun obtainEvent(event: CategoryEvent) {
        when (event) {
            is CategoryEvent.OnBackClick -> onNavigateBack()
            is CategoryEvent.OnSaveClick -> {}
            is CategoryEvent.OnChangeCategoryTypeClick -> {}
            is CategoryEvent.OnMoreCategoryIconsClick -> {}
            is CategoryEvent.OnMoreCategoryColorsClick -> {}
        }
    }

    private fun onNavigateBack() {
        _action.tryEmit(CategoryAction.NavigateBack)
    }
}
