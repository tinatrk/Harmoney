package com.example.harmoney.presentation.categoryList.models

sealed interface CategoryListAction {
    data object NavigateToCreatingCategory : CategoryListAction
    data class NavigateToOpeningCategory(val categoryId: Long?) : CategoryListAction
    data object NavigateBack : CategoryListAction
}
