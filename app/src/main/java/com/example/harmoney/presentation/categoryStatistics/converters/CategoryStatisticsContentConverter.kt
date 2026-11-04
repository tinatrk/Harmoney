package com.example.harmoney.presentation.categoryStatistics.converters

import com.example.harmoney.domain.statistics.models.StatisticsData
import com.example.harmoney.presentation.categoryStatistics.models.CategoryStatisticsContent

interface CategoryStatisticsContentConverter {
    fun map(data: StatisticsData): CategoryStatisticsContent
}
