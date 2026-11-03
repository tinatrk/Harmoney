package com.example.harmoney.domain.exchangeRate.api.useCase

import com.example.harmoney.domain.models.ConvertedTotal
import com.example.harmoney.domain.models.Currency
import com.example.harmoney.domain.models.ExchangeRateResult
import com.example.harmoney.domain.models.Money

interface CalculateConvertedTotalUseCase {
    fun execute(
        moneyList: List<Money>,
        rates: Set<ExchangeRateResult>,
        targetCurrency: Currency
    ): ConvertedTotal
}
