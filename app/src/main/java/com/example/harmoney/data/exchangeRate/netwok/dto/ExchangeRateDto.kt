package com.example.harmoney.data.exchangeRate.netwok.dto

import com.example.harmoney.data.network.BigDecimalSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.math.BigDecimal

@Serializable
data class ExchangeRateDto(
    val date: String,
    @SerialName("base") val baseIsoCode: String,
    @SerialName("quote") val quoteIsoCode: String,
    @Serializable(with = BigDecimalSerializer::class)
    val rate: BigDecimal
)
