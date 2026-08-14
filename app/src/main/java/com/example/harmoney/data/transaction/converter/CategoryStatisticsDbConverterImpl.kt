package com.example.harmoney.data.transaction.converter

import com.example.harmoney.data.transaction.dto.CategoryStatisticsDb
import com.example.harmoney.domain.models.Category
import com.example.harmoney.domain.models.CategoryColors
import com.example.harmoney.domain.models.CategoryIcon
import com.example.harmoney.domain.models.CategoryIcons
import com.example.harmoney.domain.models.CategoryStatistics
import com.example.harmoney.domain.models.CategoryType

class CategoryStatisticsDbConverterImpl : CategoryStatisticsDbConverter {
    override fun map(categoryStatistics: CategoryStatisticsDb): CategoryStatistics {
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
            totalAmount = categoryStatistics.totalAmount,
            percentage = categoryStatistics.percentage
        )
    }

    override fun map(categoryStatistics: CategoryStatistics): CategoryStatisticsDb {
        return CategoryStatisticsDb(
            id = categoryStatistics.category.id,
            name = categoryStatistics.category.name,
            typeId = categoryStatistics.category.type.id,
            iconId = categoryStatistics.category.icon.icon.id,
            iconColorId = categoryStatistics.category.icon.color.id,
            createdAt = categoryStatistics.category.createdAt,
            userOrder = categoryStatistics.category.userOrder,
            totalAmount = categoryStatistics.totalAmount,
            percentage = categoryStatistics.percentage
        )
    }
}
