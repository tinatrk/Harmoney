package com.example.harmoney.domain.exchangeRate.impl

import com.example.harmoney.core.util.Resource
import com.example.harmoney.domain.converter.ExchangeRateResultConverter
import com.example.harmoney.domain.exchangeRate.api.useCase.CalculateConvertedTotalUseCase
import com.example.harmoney.domain.exchangeRate.api.useCase.ConvertTotalMoneyUseCase
import com.example.harmoney.domain.exchangeRate.api.useCase.GetExchangeRatesUseCase
import com.example.harmoney.domain.exchangeRate.models.ExchangeRateFailure
import com.example.harmoney.domain.models.ConvertTotalResult
import com.example.harmoney.domain.models.Currency
import com.example.harmoney.domain.models.Money

class ConvertTotalMoneyUseCaseImpl(
    private val getExchangeRatesUseCase: GetExchangeRatesUseCase,
    private val calculateConvertedTotalUseCase: CalculateConvertedTotalUseCase,
    private val exchangeRateResultConverter: ExchangeRateResultConverter,
) : ConvertTotalMoneyUseCase {
    override suspend fun execute(
        moneyList: List<Money>,
        targetCurrency: Currency
    ): Resource<ConvertTotalResult, ExchangeRateFailure> {

        val currencies = moneyList.map { it.currency }.toSet() + targetCurrency

        return when (val ratesResource =
            getExchangeRatesUseCase.execute(sources = currencies, target = targetCurrency)
        ) {

            is Resource.Error -> Resource.Error(ratesResource.error)

            is Resource.Success -> {
                val rateResults = ratesResource.data.toSet()
                val rateStatuses = exchangeRateResultConverter.map(rateResults)

                val total = calculateConvertedTotalUseCase.execute(
                    moneyList,
                    rateResults,
                    targetCurrency
                )
                Resource.Success(ConvertTotalResult(total, rateStatuses))
            }
        }
    }
}
