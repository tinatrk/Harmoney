package com.example.harmoney.presentation.category.models

import androidx.compose.runtime.Stable
import com.example.harmoney.domain.models.CategoryColors
import com.example.harmoney.domain.models.CategoryIcons
import com.example.harmoney.domain.models.CategoryType

@Stable
data class CategoryState(
    val isCreateCategoryScreen: Boolean = true,

    val selectedCategoryType: CategoryType = CategoryType.EXPENSES,

    val categoryName: String = "",
    val categoryNameError: CategoryNameError = CategoryNameError.None,

    val selectedIcon: CategoryIcons = CategoryIcons.IC_SHOP_CART,
    val isIconsBottomSheetOpened: Boolean = false,

    val selectedColor: CategoryColors = CategoryColors.VIOLET_T68,

    val isSaveCategoryErrorDialogOpened: Boolean = false,
    val isCategoryNotSavedDialogOpened: Boolean = false,
    val isCategoryDeleteDialogOpened: Boolean = false,
    val initCategoryName: String = "",

    val isDataLoadingErrorDialogOpened: Boolean = false,

    val isDataReadyForEditing: Boolean = false
)
