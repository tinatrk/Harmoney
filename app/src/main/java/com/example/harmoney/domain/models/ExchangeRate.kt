package com.example.harmoney.domain.models

import java.math.BigDecimal
import java.time.LocalDate

data class ExchangeRate(
    val sourceCurrency: Currency,
    val targetCurrency: Currency,
    val rateValue: BigDecimal,
    val rateDate: LocalDate,
)
