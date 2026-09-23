package com.example.harmoney.data.statistics.converter

import com.example.harmoney.data.core.bd.dto.TotalMoneyDto
import com.example.harmoney.data.statistics.dto.CategoryStatisticsDb
import com.example.harmoney.domain.models.CategoryStatistics

interface CategoryStatisticsDbConverter {
    fun map(
        categoryStatistics: CategoryStatisticsDb,
        totals: List<TotalMoneyDto>
    ): CategoryStatistics
}
