package com.example.harmoney.presentation.converters

import com.example.harmoney.domain.models.Money
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.Locale

class NumbersFormatterImpl : NumbersFormatter {

    override fun moneyToStringWithCurrency(
        moneyInMinorUnits: Money
    ): String {
        val moneyStr = moneyToString(moneyInMinorUnits)
        return String.format(Locale.ENGLISH, "%s ${moneyInMinorUnits.currency.symbol}", moneyStr)
    }

    override fun moneyToString(
        moneyInMinorUnits: Money
    ): String {
        // convert cents to dollars, kopecks to rubles and so on
        val moneyBaseUnits = BigDecimal.valueOf(
            moneyInMinorUnits.amount.minorUnits, moneyInMinorUnits.currency.minorUnitScale
        )

        val formatter = NumberFormat.getNumberInstance(Locale.US).apply {
            minimumFractionDigits = MIN_MONEY_DECIMAL_PLACES
            maximumFractionDigits = moneyInMinorUnits.currency.minorUnitScale
            isGroupingUsed = true
        }
        return formatter
            .format(moneyBaseUnits)
            .replace(oldValue = SEPARATOR_COMMA, newValue = SEPARATOR_SPACE)
    }

    override fun percentageToString(number: BigDecimal): String {
        val roundedNumber = roundNumber(number, PERCENTAGE_DECIMAL_PLACES)
        // Используется не String.format, чтобы сохранить округление
        return "${roundedNumber}$PERCENT"
    }

    private fun roundNumber(number: BigDecimal, decimalPlaces: Int): BigDecimal {
        val rounded = number.setScale(decimalPlaces, RoundingMode.HALF_UP)

        return if (rounded.stripTrailingZeros().scale() <= 0) {
            rounded.setScale(MIN_DECIMAL_PLACES)
        } else {
            rounded
        }
    }

    override fun moneyListToStringWithCurrencies(moneyList: List<Money>): String {
        return moneyList.joinToString(MONEY_LIST_SEPARATOR) {
            moneyToStringWithCurrency(it)
        }
    }

    private companion object {
        const val SEPARATOR_COMMA = ","
        const val SEPARATOR_SPACE = " "
        const val PERCENT = "%"
        const val MIN_DECIMAL_PLACES = 0
        const val MIN_MONEY_DECIMAL_PLACES = 0
        const val PERCENTAGE_DECIMAL_PLACES = 2
        const val MONEY_LIST_SEPARATOR = ", "
    }
}
