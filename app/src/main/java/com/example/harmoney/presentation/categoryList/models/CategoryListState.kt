package com.example.harmoney.presentation.categoryList.models

import androidx.compose.runtime.Stable
import com.example.harmoney.domain.models.CategoryType

@Stable
data class CategoryListState(
    val selectedCategoryType: CategoryType = CategoryType.EXPENSES,
    val selectedTabIndex: Int = CategoryType.EXPENSES.ordinal,
    // то, что зависит от выбора таб вкладки
    val categoryInfo: String = ""
)
