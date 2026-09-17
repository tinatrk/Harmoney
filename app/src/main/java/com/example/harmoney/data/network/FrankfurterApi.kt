package com.example.harmoney.data.network

import com.example.harmoney.data.currencyList.network.dto.CurrencyDto
import com.example.harmoney.data.exchangeRate.netwok.dto.CurrencyRateDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FrankfurterApi {

    @GET("currencies")
    suspend fun getCurrencies(): retrofit2.Response<List<CurrencyDto>>

    @GET("rate/{base}/{quote}")
    suspend fun getRate(
        @Path("base") baseIsoCode: String,
        @Path("quote") quoteIsoCode: String,
        @Query("date") date: String? = null,
    ): retrofit2.Response<CurrencyRateDto>

    @GET("rates")
    suspend fun getRates(
        @Query("base") baseIsoCode: String,
        @Query("quotes") quoteIsoCodes: String? = null,
        @Query("from") from: String? = null,
        @Query("to") to: String? = null,
    ): retrofit2.Response<List<CurrencyRateDto>>
}
