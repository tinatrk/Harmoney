package com.example.harmoney.presentation.category.models

import com.example.harmoney.domain.models.CategoryType

sealed interface CategoryEvent {
    data object OnBackClick : CategoryEvent
    data object OnSaveClick : CategoryEvent
    data object OnMoreCategoryIconsClick : CategoryEvent
    data object OnMoreCategoryColorsClick : CategoryEvent
    data class OnChangeCategoryTypeClick(val categoryType: CategoryType) : CategoryEvent
}
