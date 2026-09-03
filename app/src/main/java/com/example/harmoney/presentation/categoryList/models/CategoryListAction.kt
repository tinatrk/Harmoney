package com.example.harmoney.presentation.categoryList.models

sealed interface CategoryListAction {
    data object NavigateToCreatingCategory : CategoryListAction
    data class NavigateToOpeningCategory(val categoryId: Long?) : CategoryListAction
    data object NavigateBack : CategoryListAction
    data object GetCategoryListError : CategoryListAction
    data object SetSortOptionError : CategoryListAction
    data object UpdateCategoryUserOrderError : CategoryListAction
}
