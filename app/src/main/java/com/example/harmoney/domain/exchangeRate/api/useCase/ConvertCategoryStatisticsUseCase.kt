package com.example.harmoney.domain.exchangeRate.api.useCase

import com.example.harmoney.core.util.Resource
import com.example.harmoney.domain.exchangeRate.models.ExchangeRateFailure
import com.example.harmoney.domain.models.CategoryStatistics
import com.example.harmoney.domain.models.ConvertCategoryStatisticsResult
import com.example.harmoney.domain.models.Currency

interface ConvertCategoryStatisticsUseCase {
    suspend fun execute(
        categories: List<CategoryStatistics>,
        targetCurrency: Currency
    ): Resource<ConvertCategoryStatisticsResult, ExchangeRateFailure>
}
