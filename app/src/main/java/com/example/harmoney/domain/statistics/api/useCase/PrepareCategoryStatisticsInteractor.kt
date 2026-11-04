package com.example.harmoney.domain.statistics.api.useCase

import com.example.harmoney.core.util.Resource
import com.example.harmoney.domain.exchangeRate.models.ExchangeRateFailure
import com.example.harmoney.domain.models.Currency
import com.example.harmoney.domain.statistics.models.RawStatistics
import com.example.harmoney.domain.statistics.models.StatisticsData

interface PrepareCategoryStatisticsInteractor {
    fun prepareOriginal(
        raw: RawStatistics,
        currency: Currency
    ): StatisticsData.Original

    suspend fun prepareConverted(
        raw: RawStatistics,
        targetCurrency: Currency
    ): Resource<StatisticsData.Converted, ExchangeRateFailure>
}
