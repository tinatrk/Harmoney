package com.example.harmoney.domain.statistics.impl

import com.example.harmoney.core.util.Resource
import com.example.harmoney.domain.exchangeRate.api.useCase.ConvertCategoryStatisticsUseCase
import com.example.harmoney.domain.exchangeRate.api.useCase.ConvertTotalMoneyUseCase
import com.example.harmoney.domain.exchangeRate.models.ExchangeRateFailure
import com.example.harmoney.domain.models.Currency
import com.example.harmoney.domain.statistics.api.useCase.PrepareCategoryStatisticsInteractor
import com.example.harmoney.domain.statistics.calculator.MoneyCalculator
import com.example.harmoney.domain.statistics.models.ConvertedStatistics
import com.example.harmoney.domain.statistics.models.OriginalCategoryStatistics
import com.example.harmoney.domain.statistics.models.OriginalStatistics
import com.example.harmoney.domain.statistics.models.RawStatistics
import com.example.harmoney.domain.statistics.models.StatisticsData

class PrepareCategoryStatisticsInteractorImpl(
    private val moneyCalculator: MoneyCalculator,
    private val convertCategoryStatisticsUseCase: ConvertCategoryStatisticsUseCase,
    private val convertTotalMoneyUseCase: ConvertTotalMoneyUseCase,
) : PrepareCategoryStatisticsInteractor {
    override fun prepareOriginal(
        raw: RawStatistics,
        currency: Currency
    ): StatisticsData.Original {
        val totalByCategoryType =
            moneyCalculator.getTotalAmountWithOneCurrency(
                amounts = raw.total,
                currency = currency
            )

        val categories = raw.categories.map {
            OriginalCategoryStatistics(
                category = it.category,
                total = moneyCalculator
                    .getTotalAmountWithOneCurrency(
                        it.totalAmounts,
                        totalByCategoryType.currency
                    )
            )
        }
        return StatisticsData.Original(
            OriginalStatistics(
                categories, totalByCategoryType
            )
        )
    }

    override suspend fun prepareConverted(
        raw: RawStatistics,
        targetCurrency: Currency
    ): Resource<StatisticsData.Converted, ExchangeRateFailure> {
        val categoriesResource = convertCategoryStatisticsUseCase.execute(
            categories = raw.categories,
            targetCurrency = targetCurrency,
        )

        return when (categoriesResource) {
            is Resource.Error -> Resource.Error(categoriesResource.error)

            is Resource.Success -> {
                val totalResource = convertTotalMoneyUseCase.execute(
                    moneyList = raw.total,
                    targetCurrency = targetCurrency
                )

                when (totalResource) {
                    is Resource.Error -> Resource.Error(totalResource.error)

                    is Resource.Success -> {
                        Resource.Success(
                            StatisticsData.Converted(
                                ConvertedStatistics(
                                    categories = categoriesResource.data.categoryStatistics,
                                    total = totalResource.data.total,
                                    exchangeRateStatuses = categoriesResource.data.exchangeRates
                                            + totalResource.data.exchangeRates
                                )
                            )
                        )
                    }
                }
            }
        }
    }
}
