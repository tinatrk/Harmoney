package com.example.harmoney.presentation.converters

import com.example.harmoney.domain.models.ConvertedCategoryStatistics
import com.example.harmoney.domain.models.ConvertedTotal
import com.example.harmoney.domain.models.Money
import com.example.harmoney.presentation.models.CategoryStatisticsUi
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import java.math.BigDecimal
import java.math.RoundingMode

class CategoryStatisticsUiConverterImpl(
    private val categoryUiConverter: CategoryUiConverter,
    private val numberFormatter: NumbersFormatter,
    private val convertedTotalFormatter: ConvertedTotalFormatter,
) :
    CategoryStatisticsUiConverter {

    override fun map(
        categoryStatistics: ConvertedCategoryStatistics, totalByCategoryType: ConvertedTotal,
    ): CategoryStatisticsUi {
        return CategoryStatisticsUi(
            category = categoryUiConverter.map(categoryStatistics.category),
            totalAmount = numberFormatter.moneyToStringWithCurrency(
                categoryStatistics.total.total
            ),
            totalOrigins = convertedTotalFormatter.getOriginalsStr(categoryStatistics.total),
            percentage = getPercentageString(
                categoryStatistics.total.total,
                totalByCategoryType.total
            ),
        )
    }

    private fun getPercentageString(category: Money, total: Money): String {
        if (category.currency.id != total.currency.id) return EMPTY_STRING

        return numberFormatter.percentageToString(getPercentage(category, total))
    }

    private fun getPercentage(category: Money, total: Money): BigDecimal {
        if (total.amount.minorUnits == 0L) return BigDecimal.ZERO

        return category.amount.minorUnits.toBigDecimal()
            .multiply(BigDecimal.valueOf(100))
            .divide(total.amount.minorUnits.toBigDecimal(), 2, RoundingMode.HALF_UP)
    }

    override fun map(
        categoryStatisticsList: List<ConvertedCategoryStatistics>,
        totalByCategoryType: ConvertedTotal
    ): ImmutableList<CategoryStatisticsUi> {
        return categoryStatisticsList
            .map { map(it, totalByCategoryType) }.toImmutableList()
    }

    companion object {
        private const val EMPTY_STRING = ""
    }
}
