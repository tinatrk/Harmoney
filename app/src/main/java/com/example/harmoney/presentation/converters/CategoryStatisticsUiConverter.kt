package com.example.harmoney.presentation.converters

import com.example.harmoney.domain.models.CategoryStatistics
import com.example.harmoney.domain.models.Currency
import com.example.harmoney.presentation.models.CategoryStatisticsUi
import kotlinx.collections.immutable.ImmutableList

interface CategoryStatisticsUiConverter {
    fun map(categoryStatistics: CategoryStatistics, currency: Currency): CategoryStatisticsUi
    fun map(
        categories: List<CategoryStatistics>,
        currency: Currency
    ): ImmutableList<CategoryStatisticsUi>
}
