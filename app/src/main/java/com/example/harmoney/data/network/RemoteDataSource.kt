package com.example.harmoney.data.network

import com.example.harmoney.data.currencyList.network.dto.CurrencyDto
import com.example.harmoney.data.exchangeRate.netwok.dto.ExchangeRateDto

interface RemoteDataSource {

    suspend fun getCurrencies(): NetworkResult<List<CurrencyDto>>

    suspend fun getRate(
        baseIsoCode: String,
        quoteIsoCode: String,
        date: String? = null,
    ): NetworkResult<ExchangeRateDto>

    suspend fun getRates(
        baseIsoCode: String,
        quoteIsoCodes: String,
        from: String? = null,
        to: String? = null,
    ): NetworkResult<List<ExchangeRateDto>>
}
