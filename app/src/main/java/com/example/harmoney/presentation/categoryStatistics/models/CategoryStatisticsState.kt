package com.example.harmoney.presentation.categoryStatistics.models

import com.example.harmoney.domain.models.CategoryType

data class CategoryStatisticsState(
    val currentBalance: Double = 0.0,
    val categoryType: CategoryType = CategoryType.Expenses,
    val selectedTabIndex: Int = CategoryType.Expenses.ordinal,
    // то, что зависит от выбора таб вкладки
    val categoryInfo: String = ""
)
