package com.example.harmoney.presentation.categoryStatistics.converters

import com.example.harmoney.domain.models.Category
import com.example.harmoney.domain.models.ConvertedTotal
import com.example.harmoney.domain.models.Money
import com.example.harmoney.presentation.converters.CategoryUiConverter
import com.example.harmoney.presentation.converters.ConvertedTotalFormatter
import com.example.harmoney.presentation.converters.NumbersFormatter
import com.example.harmoney.presentation.models.CategoryStatisticsUi
import java.math.BigDecimal

class CategoryStatisticsUiConverterImpl(
    private val categoryUiConverter: CategoryUiConverter,
    private val numberFormatter: NumbersFormatter,
    private val convertedTotalFormatter: ConvertedTotalFormatter,
) : CategoryStatisticsUiConverter {

    override fun map(
        category: Category,
        total: ConvertedTotal,
        percentage: BigDecimal,
    ): CategoryStatisticsUi {
        return CategoryStatisticsUi(
            category = categoryUiConverter.map(category),
            totalAmount = numberFormatter.moneyToStringWithCurrency(total.total),
            totalOrigins = convertedTotalFormatter.getOriginalsStr(total),
            percentage = numberFormatter.percentageToString(percentage, PERCENTAGE_SCALE)
        )
    }

    override fun map(
        category: Category,
        total: Money,
        percentage: BigDecimal
    ): CategoryStatisticsUi {
        return CategoryStatisticsUi(
            category = categoryUiConverter.map(category),
            totalAmount = numberFormatter.moneyToStringWithCurrency(total),
            totalOrigins = null,
            percentage = numberFormatter.percentageToString(percentage, PERCENTAGE_SCALE)
        )
    }

    companion object {
        private const val PERCENTAGE_SCALE = 1
    }
}
