package com.example.harmoney.domain.exchangeRate.api.useCase

import com.example.harmoney.core.util.Resource
import com.example.harmoney.domain.exchangeRate.models.ExchangeRateFailure
import com.example.harmoney.domain.models.ConvertTotalResult
import com.example.harmoney.domain.models.Currency
import com.example.harmoney.domain.models.Money

interface ConvertTotalMoneyUseCase {
    suspend fun execute(
        moneyList: List<Money>,
        targetCurrency: Currency
    ): Resource<ConvertTotalResult, ExchangeRateFailure>
}
