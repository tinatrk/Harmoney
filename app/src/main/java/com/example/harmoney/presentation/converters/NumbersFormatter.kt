package com.example.harmoney.presentation.converters

import com.example.harmoney.domain.models.Money
import java.math.BigDecimal

interface NumbersFormatter {
    /**
     * `moneyInMinorUnits` - the amount of money in the smallest monetary units for a specific
     * currency (cents for USD, kopecks for RUS and so on)
     *
     * Examples:
     *
     * `123` (kopecks/cents) -> `1.23 ₽` (rubles/dollars)
     *
     * `123456` (kopecks/cents) -> `1 234.56 ₽` (rubles/dollars)
     *
     * `12300` (kopecks/cents) -> `123 ₽` (rubles/dollars)
     *
     * `12340` (kopecks/cents) -> `123.4 ₽` (rubles/dollars)
     *  */
    fun moneyToStringWithCurrency(
        moneyInMinorUnits: Money
    ): String

    /**
     * `moneyList` - list of the amounts of money in the smallest monetary units for a specific
     * currency (cents for USD, kopecks for RUS and so on)
     *
     * Examples:
     *
     * `[123, 456]` (kopecks/cents) -> `1.23 ₽, 4.56 $` (rubles/dollars)
     *
     * `[123456, 789012]` (kopecks/cents) -> `1 234.56 ₽, 7 890.12 $` (rubles/dollars)
     *
     * `[12300, 45600]` (kopecks/cents) -> `123 ₽, 456 $` (rubles/dollars)
     *
     * `[12340, 56780]` (kopecks/cents) -> `123.4 ₽, 567.8 $` (rubles/dollars)
     *  */
    fun moneyListToStringWithCurrencies(
        moneyList: List<Money>
    ): String

    /**
     * `moneyInMinorUnits` - the amount of money in the smallest monetary units for a specific
     * currency (cents for USD, kopecks for RUS and so on)
     *
     * Examples:
     *
     * `123` (kopecks/cents) -> `1.23` (rubles/dollars)
     *
     * `123456` (kopecks/cents) -> `1 234.56` (rubles/dollars)
     *
     * `12300` (kopecks/cents) -> `123` (rubles/dollars)
     *
     * `12340` (kopecks/cents) -> `123.4` (rubles/dollars)
     *  */
    fun moneyToString(
        moneyInMinorUnits: Money
    ): String

    /**
     * Examples:
     *
     * `56,6` -> `56.6%`
     *
     * `56,66` -> `56.66%`
     *
     * `56.666` -> `56.67%`
     *
     * `56.60` -> `56.6%`
     */
    fun percentageToString(number: BigDecimal): String
}
