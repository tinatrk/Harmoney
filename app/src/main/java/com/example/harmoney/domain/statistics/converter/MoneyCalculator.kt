package com.example.harmoney.domain.statistics.converter

import com.example.harmoney.domain.models.Currency
import com.example.harmoney.domain.models.Money
import java.math.BigDecimal

interface MoneyCalculator {
    fun getTotalAmountWithOneCurrency(
        amounts: List<Money>,
        currency: Currency
    ): Money

    fun getPercentage(value: Money, total: Money): BigDecimal
}
