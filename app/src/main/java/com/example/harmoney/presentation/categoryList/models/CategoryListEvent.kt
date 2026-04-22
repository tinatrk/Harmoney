package com.example.harmoney.presentation.categoryList.models

import com.example.harmoney.domain.models.CategoryType

sealed class CategoryListEvent {
    data object OnBackClick : CategoryListEvent()
    data class OnTabClick(val categoryType: CategoryType) : CategoryListEvent()
    data class OnCategoryClick(val categoryId: Long) : CategoryListEvent()
    data object OnFloatingButtonClick : CategoryListEvent()
}
