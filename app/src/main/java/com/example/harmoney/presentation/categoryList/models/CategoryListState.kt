package com.example.harmoney.presentation.categoryList.models

import com.example.harmoney.domain.models.CategoryType

data class CategoryListState(
    val isChoiceCategoryScreen: Boolean = true,
    val categoryType: CategoryType = CategoryType.Expenses,
    val selectedTabIndex: Int = CategoryType.Expenses.ordinal,
    // то, что зависит от выбора таб вкладки
    val categoryInfo: String = ""
)
