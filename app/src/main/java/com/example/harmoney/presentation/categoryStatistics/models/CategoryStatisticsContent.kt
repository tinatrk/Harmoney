package com.example.harmoney.presentation.categoryStatistics.models

import com.example.harmoney.presentation.models.CategoryStatisticsUi
import com.example.harmoney.presentation.models.PieChartItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class CategoryStatisticsContent(
    val categories: ImmutableList<CategoryStatisticsUi> = persistentListOf(),
    val pieChartCategories: ImmutableList<PieChartItem> = persistentListOf(),
    val totalByCategoryType: String = "0.00",
    val totalOriginalsByCategoryType: String? = null,
)
