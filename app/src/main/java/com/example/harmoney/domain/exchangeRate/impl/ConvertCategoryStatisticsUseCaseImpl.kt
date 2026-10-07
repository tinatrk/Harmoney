package com.example.harmoney.domain.exchangeRate.impl

import com.example.harmoney.core.util.Resource
import com.example.harmoney.domain.converter.ExchangeRateResultConverter
import com.example.harmoney.domain.exchangeRate.api.useCase.ConvertCategoryStatisticsUseCase
import com.example.harmoney.domain.exchangeRate.api.useCase.GetConvertedTotalByMoneyListUseCase
import com.example.harmoney.domain.exchangeRate.api.useCase.GetExchangeRatesUseCase
import com.example.harmoney.domain.exchangeRate.models.ExchangeRateFailure
import com.example.harmoney.domain.models.CategoryStatistics
import com.example.harmoney.domain.models.ConvertCategoryStatisticsResult
import com.example.harmoney.domain.models.ConvertedCategoryStatistics
import com.example.harmoney.domain.models.Currency

class ConvertCategoryStatisticsUseCaseImpl(
    private val getExchangeRatesUseCase: GetExchangeRatesUseCase,
    private val getConvertedTotalByMoneyListUseCase: GetConvertedTotalByMoneyListUseCase,
    private val exchangeRateResultConverter: ExchangeRateResultConverter,
) : ConvertCategoryStatisticsUseCase {
    override suspend fun execute(
        categories: List<CategoryStatistics>,
        targetCurrency: Currency
    ): Resource<ConvertCategoryStatisticsResult, ExchangeRateFailure> {
        val currencies: MutableSet<Currency> = mutableSetOf()
        categories.forEach { category ->
            currencies.addAll(category.totalAmounts.map { it.currency })
        }
        currencies.add(targetCurrency)

        return when (val ratesResource =
            getExchangeRatesUseCase.execute(sources = currencies, target = targetCurrency)
        ) {
            is Resource.Error -> Resource.Error(ratesResource.error)

            is Resource.Success -> {
                val rateResults = ratesResource.data.toSet()
                val rateStatuses = exchangeRateResultConverter.map(rateResults)

                val converted = categories.map { category ->
                    ConvertedCategoryStatistics(
                        category = category.category,
                        total = getConvertedTotalByMoneyListUseCase.execute(
                            moneyList = category.totalAmounts,
                            rates = rateResults,
                            targetCurrency = targetCurrency,
                        )
                    )
                }
                Resource.Success(
                    ConvertCategoryStatisticsResult(
                        categoryStatistics = converted, exchangeRates = rateStatuses
                    )
                )
            }
        }
    }
}
