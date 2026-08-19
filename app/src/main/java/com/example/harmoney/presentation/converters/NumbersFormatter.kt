package com.example.harmoney.presentation.converters

import com.example.harmoney.domain.models.Currency

interface NumbersFormatter {
    /**
     * `moneyMinorUnits` - the amount of money in the smallest monetary units for a specific
     * currency (cents for USD, kopecks for RUS and so on)
     *
     * Examples:
     *
     * `123` (cents/kopecks) -> `1.23 ₽` (dollars/rubles)
     *
     * `123456` (cents/kopecks) -> `1 234.56 ₽` (dollars/rubles)
     *
     * `12300` (cents/kopecks) -> `123 ₽` (dollars/rubles)
     *
     * `12340` (cents/kopecks) -> `123.4 ₽` (dollars/rubles)
     *  */
    fun moneyToStringWithCurrency(
        moneyMinorUnits: Long,
        currency: Currency
    ): String

    /**
     * `moneyMinorUnits` - the amount of money in the smallest monetary units for a specific
     * currency (cents for USD, kopecks for RUS and so on)
     *
     * Examples:
     *
     * `123` (cents/kopecks) -> `1.23` (dollars/rubles)
     *
     * `123456` (cents/kopecks) -> `1 234.56` (dollars/rubles)
     *
     * `12300` (cents/kopecks) -> `123` (dollars/rubles)
     *
     * `12340` (cents/kopecks) -> `123.4` (dollars/rubles)
     *  */
    fun moneyToString(
        moneyMinorUnits: Long,
        currency: Currency
    ): String

    /**
     * Examples:
     *
     * `56,6` -> `56.6%`
     *
     * `56,66` -> `56.66%`
     *
     * `56.666` -> `56.67%`
     */
    fun percentageToString(number: Float): String
}
