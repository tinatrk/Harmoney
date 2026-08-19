package com.example.harmoney.domain.models

import kotlin.math.pow
import kotlin.math.roundToLong

/**
 * `minorUnits` - the amount of money in the smallest monetary units for a specific currency
 * (cents for USD, kopecks for RUS and so on)
 * */
data class Money(val minorUnits: Long) {
    companion object {
        fun fromDouble(value: Double, currency: Currency): Money {
            val factor = 10.0.pow(currency.minorUnitScale)

            return Money(minorUnits = (value * factor).roundToLong())
        }
    }
}
