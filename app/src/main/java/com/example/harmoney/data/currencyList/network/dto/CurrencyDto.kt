package com.example.harmoney.data.currencyList.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrencyDto(
    @SerialName("iso_code") val isoCode: String,
    @SerialName("name") val name: String,
    @SerialName("iso_numeric") val isoNumeric: String?,
    @SerialName("symbol") val symbol: String?,
    @SerialName("start_date") val startDate: String?,
    @SerialName("end_date") val endDate: String?
)
