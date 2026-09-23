package com.example.harmoney.data.statistics.converter

import com.example.harmoney.data.core.bd.converter.TotalMoneyDtoConverter
import com.example.harmoney.data.core.bd.dto.TotalMoneyDto
import com.example.harmoney.data.statistics.dto.CategoryStatisticsDb
import com.example.harmoney.domain.models.Category
import com.example.harmoney.domain.models.CategoryColors
import com.example.harmoney.domain.models.CategoryIcon
import com.example.harmoney.domain.models.CategoryIcons
import com.example.harmoney.domain.models.CategoryStatistics
import com.example.harmoney.domain.models.CategoryType

class CategoryStatisticsDbConverterImpl(private val totalMoneyDtoConverter: TotalMoneyDtoConverter) :
    CategoryStatisticsDbConverter {
    override fun map(
        categoryStatistics: CategoryStatisticsDb,
        totals: List<TotalMoneyDto>
    ): CategoryStatistics {
        return CategoryStatistics(
            category = Category(
                id = categoryStatistics.id,
                name = categoryStatistics.name,
                type = CategoryType.fromId(categoryStatistics.typeId),
                icon = CategoryIcon(
                    icon = CategoryIcons.fromId(categoryStatistics.iconId),
                    color = CategoryColors.fromId(categoryStatistics.iconColorId)
                ),
                createdAt = categoryStatistics.createdAt,
                userOrder = categoryStatistics.userOrder
            ),
            totalAmounts = totals.map { totalMoneyDtoConverter.map(it) }
        )
    }
}
