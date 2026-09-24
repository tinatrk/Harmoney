package com.example.harmoney.domain.models

import java.math.BigDecimal
import java.time.LocalDate

data class ExchangeRate(
    val baseCurrency: Currency,
    val quoteCurrency: Currency,
    val rate: BigDecimal,
    val rateDate: LocalDate,
)
