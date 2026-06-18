package com.example.harmoney.presentation.categoryList.models

import com.example.harmoney.domain.models.CategorySortOption
import com.example.harmoney.domain.models.CategoryType

sealed class CategoryListEvent {
    data object OnBackClick : CategoryListEvent()

    data object OnSortMenuClick : CategoryListEvent()
    data class OnSortOptionClick(val newSortOption: CategorySortOption) : CategoryListEvent()

    data class OnTabClick(val newCategoryType: CategoryType) : CategoryListEvent()

    data class OnCategoryClick(val categoryId: Long) : CategoryListEvent()

    data object OnFloatingButtonClick : CategoryListEvent()
}
