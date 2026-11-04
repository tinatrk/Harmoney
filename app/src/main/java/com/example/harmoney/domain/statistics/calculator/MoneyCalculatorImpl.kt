package com.example.harmoney.domain.statistics.calculator

import com.example.harmoney.domain.models.Currency
import com.example.harmoney.domain.models.Money
import com.example.harmoney.domain.models.MoneyAmount
import java.math.BigDecimal
import java.math.RoundingMode

class MoneyCalculatorImpl : MoneyCalculator {
    override fun getTotalAmountWithOneCurrency(
        amounts: List<Money>,
        currency: Currency
    ): Money {
        require(amounts.all { it.currency.id == currency.id }) {
            "Cannot sum money with different currencies"
        }

        return Money(
            amount = MoneyAmount(amounts.sumOf { it.amount.minorUnits }),
            currency = currency
        )
    }

    override fun getPercentage(value: Money, total: Money): BigDecimal {
        if (total.amount.minorUnits == 0L || value.currency.id != total.currency.id)
            return BigDecimal.ZERO

        return value.amount.minorUnits.toBigDecimal()
            .multiply(BigDecimal.valueOf(PERCENTAGE_MULTIPLY_CONST))
            .divide(total.amount.minorUnits.toBigDecimal(), 2, RoundingMode.HALF_UP)

    }

    companion object {
        private const val PERCENTAGE_MULTIPLY_CONST = 100L
    }
}
