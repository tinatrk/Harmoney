package com.example.harmoney.presentation.converters

import com.example.harmoney.domain.models.ConvertedCategoryStatistics
import com.example.harmoney.domain.models.ConvertedTotal
import com.example.harmoney.presentation.models.CategoryStatisticsUi
import kotlinx.collections.immutable.ImmutableList

interface CategoryStatisticsUiConverter {
    fun map(
        categoryStatistics: ConvertedCategoryStatistics,
        totalByCategoryType: ConvertedTotal,
    ): CategoryStatisticsUi

    fun map(
        categoryStatisticsList: List<ConvertedCategoryStatistics>,
        totalByCategoryType: ConvertedTotal
    ): ImmutableList<CategoryStatisticsUi>
}
