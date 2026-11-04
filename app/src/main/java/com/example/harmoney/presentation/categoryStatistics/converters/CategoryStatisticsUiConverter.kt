package com.example.harmoney.presentation.categoryStatistics.converters

import com.example.harmoney.domain.models.Category
import com.example.harmoney.domain.models.ConvertedTotal
import com.example.harmoney.domain.models.Money
import com.example.harmoney.presentation.models.CategoryStatisticsUi
import java.math.BigDecimal

interface CategoryStatisticsUiConverter {
    fun map(
        category: Category,
        total: ConvertedTotal,
        percentage: BigDecimal,
    ): CategoryStatisticsUi

    fun map(
        category: Category,
        total: Money,
        percentage: BigDecimal
    ): CategoryStatisticsUi
}
