package com.example.harmoney.presentation.categoryStatistics.converters

import com.example.harmoney.domain.statistics.calculator.MoneyCalculator
import com.example.harmoney.domain.statistics.models.StatisticsData
import com.example.harmoney.presentation.categoryStatistics.models.CategoryStatisticsContent
import com.example.harmoney.presentation.converters.ConvertedTotalFormatter
import com.example.harmoney.presentation.converters.NumbersFormatter
import com.example.harmoney.presentation.models.CategoryStatisticsUi
import com.example.harmoney.presentation.models.PieChartItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import java.math.BigDecimal

class CategoryStatisticsContentConverterImpl(
    private val numbersFormatter: NumbersFormatter,
    private val moneyCalculator: MoneyCalculator,
    private val categoryStatisticsUiConverter: CategoryStatisticsUiConverter,
    private val convertedTotalFormatter: ConvertedTotalFormatter,
) : CategoryStatisticsContentConverter {

    private data class PieChartSource(
        /** `value` - доля от общего (значение от 0.0 до 1.0) */
        val value: BigDecimal,
        val valueStr: String,
        val colorValue: Long
    )

    override fun map(data: StatisticsData): CategoryStatisticsContent {
        val content = when (data) {
            is StatisticsData.Original -> getOriginalContent(data)

            is StatisticsData.Converted -> getConvertedContent(data)
        }

        return content
    }

    private fun getOriginalContent(data: StatisticsData.Original): CategoryStatisticsContent {
        val formattedCategories: MutableList<CategoryStatisticsUi> = mutableListOf()
        val pieChartSources: MutableList<PieChartSource> = mutableListOf()

        val categories =
            data.statistics.categories.sortedByDescending { it.total.amount.minorUnits }

        val totalByCategoryTypeStr =
            numbersFormatter.moneyToStringWithCurrency(data.statistics.total)

        val totalOriginsByCategoryTypeStr = null

        categories.forEach { item ->
            val percentage = moneyCalculator.getPercentage(
                item.total,
                data.statistics.total
            )

            val formatted = categoryStatisticsUiConverter.map(
                item.category,
                item.total,
                percentage
            )
            formattedCategories.add(formatted)
            pieChartSources.add(
                createPieChartSource(percentage, formatted)
            )
        }

        return CategoryStatisticsContent(
            categories = formattedCategories.toImmutableList(),
            pieChartCategories = getPieChartCategories(pieChartSources),
            totalByCategoryType = totalByCategoryTypeStr,
            totalOriginalsByCategoryType = totalOriginsByCategoryTypeStr
        )
    }

    private fun getConvertedContent(data: StatisticsData.Converted): CategoryStatisticsContent {
        val formattedCategories: MutableList<CategoryStatisticsUi> = mutableListOf()
        val pieChartSources: MutableList<PieChartSource> = mutableListOf()

        val categories =
            data.statistics.categories.sortedByDescending { it.total.total.amount.minorUnits }

        val totalByCategoryTypeStr =
            numbersFormatter.moneyToStringWithCurrency(
                data.statistics.total.total
            )

        val totalOriginsByCategoryTypeStr =
            convertedTotalFormatter.getOriginalsStr(data.statistics.total)

        categories.forEach { item ->
            val percentage = moneyCalculator.getPercentage(
                item.total.total,
                data.statistics.total.total
            )

            val formatted = categoryStatisticsUiConverter.map(
                item.category,
                item.total,
                percentage
            )
            formattedCategories.add(formatted)
            pieChartSources.add(
                createPieChartSource(percentage, formatted)
            )
        }
        return CategoryStatisticsContent(
            categories = formattedCategories.toImmutableList(),
            pieChartCategories = getPieChartCategories(pieChartSources),
            totalByCategoryType = totalByCategoryTypeStr,
            totalOriginalsByCategoryType = totalOriginsByCategoryTypeStr
        )
    }

    private fun createPieChartSource(
        percentage: BigDecimal,
        category: CategoryStatisticsUi,
    ): PieChartSource = PieChartSource(
        value = percentage.movePointLeft(2),
        valueStr = category.totalAmount,
        colorValue = category.category.icon.color.background
    )

    private fun getPieChartCategories(categories: List<PieChartSource>)
            : ImmutableList<PieChartItem> {
        val startAngle = START_ANGLE.toBigDecimal()

        val pieChartItems: MutableList<PieChartItem> = mutableListOf()

        var curStartAngle = startAngle
        for (category in categories) {
            val rawSweep = category.value.multiply(MAX_ANGLE.toBigDecimal())
            val sweepAngle =
                (rawSweep - GAP_ANGLE.toBigDecimal())
                    .coerceAtLeast(MIN_ANGLE.toBigDecimal())

            pieChartItems.add(
                PieChartItem(
                    value = category.valueStr,
                    colorValue = category.colorValue,
                    startAngle = curStartAngle.toFloat(),
                    sweepAngle = sweepAngle.toFloat()
                )
            )

            curStartAngle += rawSweep
        }

        return pieChartItems.toImmutableList()
    }

    private companion object {
        const val START_ANGLE = -90
        const val GAP_ANGLE = 2
        const val MIN_ANGLE = 0
        const val MAX_ANGLE = 360
    }
}
