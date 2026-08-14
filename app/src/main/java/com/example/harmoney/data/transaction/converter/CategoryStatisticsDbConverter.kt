package com.example.harmoney.data.transaction.converter

import com.example.harmoney.data.transaction.dto.CategoryStatisticsDb
import com.example.harmoney.domain.models.CategoryStatistics

interface CategoryStatisticsDbConverter {
    fun map(categoryStatistics: CategoryStatisticsDb): CategoryStatistics

    fun map(categoryStatistics: CategoryStatistics): CategoryStatisticsDb

    fun map(categoryStatisticsList: List<CategoryStatisticsDb>) : List<CategoryStatistics>
}
