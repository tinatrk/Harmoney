package com.example.harmoney.presentation.categoryList.models

sealed interface CategoryListAction {
    data class NavigateToCreatingCategory(val categoryTypeId: Long?) : CategoryListAction
    data class NavigateToOpeningCategory(val categoryId: Long?) : CategoryListAction
    data object NavigateBack : CategoryListAction
}
