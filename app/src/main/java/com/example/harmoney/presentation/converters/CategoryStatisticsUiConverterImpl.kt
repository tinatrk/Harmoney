package com.example.harmoney.presentation.converters

import com.example.harmoney.domain.models.CategoryStatistics
import com.example.harmoney.domain.models.Currency
import com.example.harmoney.presentation.models.CategoryStatisticsUi
import com.example.harmoney.presentation.models.DecimalPlaces
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

class CategoryStatisticsUiConverterImpl(
    private val categoryUiConverter: CategoryUiConverter,
    private val numberFormatter: NumbersFormatter
) :
    CategoryStatisticsUiConverter {

    override fun map(
        categoryStatistics: CategoryStatistics,
        currency: Currency
    ): CategoryStatisticsUi {
        return CategoryStatisticsUi(
            category = categoryUiConverter.map(categoryStatistics.category),
            totalAmount = numberFormatter.toStringWithCurrency(
                number = categoryStatistics.totalAmount,
                decimalPlaces = DecimalPlaces.MONEY_DISPLAY,
                currency = currency,
                isNeededThousandSeparator = true
            ),
            percentage = numberFormatter.toStringWithPercent(
                categoryStatistics.percentage.toDouble(),
                DecimalPlaces.PERCENT_DISPLAY
            )
        )
    }

    override fun map(
        categories: List<CategoryStatistics>,
        currency: Currency
    ): ImmutableList<CategoryStatisticsUi> {
        return categories.map { map(it, currency) }.toImmutableList()
    }
}
