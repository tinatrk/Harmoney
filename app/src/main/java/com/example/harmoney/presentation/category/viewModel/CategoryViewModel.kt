package com.example.harmoney.presentation.category.viewModel

import com.example.harmoney.base.BaseViewModel
import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.presentation.category.models.CategoryAction
import com.example.harmoney.presentation.category.models.CategoryEvent
import com.example.harmoney.presentation.category.models.CategoryState

class CategoryViewModel(categoryType: CategoryType, categoryId: Long?) :
    BaseViewModel<CategoryEvent, CategoryAction, CategoryState>(
        CategoryState(
            selectedCategoryType = categoryType,
            isCreateCategoryScreen = categoryId == null
        )
    ) {
    override val tag: String = CategoryViewModel::class.java.simpleName ?: ""

    init {}

    override fun obtainEvent(event: CategoryEvent) {
        when (event) {
            is CategoryEvent.OnBackClick -> onNavigateBack()
            is CategoryEvent.OnBackDialogConfirm -> {}
            is CategoryEvent.OnBackDialogDismiss -> {}

            is CategoryEvent.OnChangeCategoryTypeClick -> {}

            is CategoryEvent.OnCategoryNameChanged -> {}

            is CategoryEvent.OnOpenIconsBottomSheetClick -> {}
            is CategoryEvent.OnIconsBottomSheetDismiss -> {}
            is CategoryEvent.OnIconClick -> {}

            is CategoryEvent.OnColorClick -> {}

            is CategoryEvent.OnSaveClick -> {}
            is CategoryEvent.OnSaveDialogDismiss -> {}

            is CategoryEvent.OnDeleteClick -> {}
            is CategoryEvent.OnDeleteDialogConfirm -> {}
            is CategoryEvent.OnDeleteDialogDismiss -> {}
        }
    }

    private fun onNavigateBack() {
        writableAction.tryEmit(CategoryAction.NavigateBack)
    }
}
