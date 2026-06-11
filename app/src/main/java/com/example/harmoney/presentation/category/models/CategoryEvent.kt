package com.example.harmoney.presentation.category.models

import com.example.harmoney.domain.models.CategoryColors
import com.example.harmoney.domain.models.CategoryIcons
import com.example.harmoney.domain.models.CategoryType

sealed interface CategoryEvent {
    data object OnBackClick : CategoryEvent
    data object OnBackDialogConfirm : CategoryEvent
    data object OnBackDialogDismiss : CategoryEvent

    data class OnChangeCategoryTypeClick(val categoryType: CategoryType) : CategoryEvent

    data class OnCategoryNameChanged(val newName: String) : CategoryEvent

    data object OnOpenIconsBottomSheetClick : CategoryEvent
    data object OnIconsBottomSheetDismiss : CategoryEvent
    data class OnIconClick(val newIcon: CategoryIcons) : CategoryEvent

    data class OnColorClick(val newColor: CategoryColors) : CategoryEvent

    data object OnSaveClick : CategoryEvent
    data object OnSaveDialogDismiss : CategoryEvent

    data object OnDeleteClick : CategoryEvent
    data object OnDeleteDialogConfirm : CategoryEvent
    data object OnDeleteDialogDismiss : CategoryEvent
}
