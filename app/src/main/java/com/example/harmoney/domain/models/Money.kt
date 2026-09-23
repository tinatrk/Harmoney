package com.example.harmoney.domain.models

import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.pow
import kotlin.math.roundToLong

data class Money(
    val amount: MoneyAmount,
    val currency: Currency,
) {
    companion object {
        fun fromDouble(value: Double, currency: Currency): Money {
            val factor = 10.0.pow(currency.minorUnitScale)

            return Money(
                amount = MoneyAmount(minorUnits = (value * factor).roundToLong()),
                currency = currency
            )
        }

        fun fromBigDecimal(
            value: BigDecimal,
            currency: Currency
        ): Money {
            val minorUnits = value
                .movePointRight(currency.minorUnitScale)
                .setScale(0, RoundingMode.HALF_UP)
                .longValueExact()

            return Money(
                amount = MoneyAmount(minorUnits),
                currency = currency
            )
        }
    }
}

/**
 * `minorUnits` - the amount of money in the smallest monetary units for a specific currency
 * (cents for USD, kopecks for RUS and so on)
 * */
@JvmInline
value class MoneyAmount(val minorUnits: Long)
