package com.example.harmoney.presentation.category.models

import androidx.compose.runtime.Stable
import com.example.harmoney.domain.models.CategoryType

@Stable
data class CategoryState(
    val categoryId: Long? = null,
    val isCreateCategoryScreen: Boolean = true,
    val selectedCategoryType: CategoryType = CategoryType.Expenses,
    val selectedTabIndex: Int = CategoryType.Expenses.ordinal,
)
