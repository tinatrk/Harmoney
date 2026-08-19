package com.example.harmoney.presentation.converters

import com.example.harmoney.domain.models.Currency
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.Locale

class NumbersFormatterImpl : NumbersFormatter {

    override fun moneyToStringWithCurrency(
        moneyMinorUnits: Long,
        currency: Currency,
    ): String {
        val moneyStr = moneyToString(moneyMinorUnits, currency)
        return String.format(Locale.ENGLISH, "%s ${currency.symbol}", moneyStr)
    }

    override fun moneyToString(
        moneyMinorUnits: Long,
        currency: Currency
    ): String {
        // convert cents to dollars, kopecks to rubles and so on
        val money = BigDecimal.valueOf(
            moneyMinorUnits, currency.minorUnitScale
        )

        val formatter = NumberFormat.getNumberInstance(Locale.US).apply {
            minimumFractionDigits = MIN_MONEY_DECIMAL_PLACES
            maximumFractionDigits = currency.minorUnitScale
            isGroupingUsed = true
        }
        return formatter
            .format(money)
            .replace(oldValue = SEPARATOR_COMMA, newValue = SEPARATOR_SPACE)
    }

    override fun percentageToString(number: Float): String {
        val roundedNumber = roundNumber(number, PERCENTAGE_DECIMAL_PLACES)
        // Используется не String.format, чтобы сохранить округление
        return "${roundedNumber}$PERCENT"
    }

    private fun roundNumber(number: Float, decimalPlaces: Int): BigDecimal {
        val longres = number.toLong()
        val digits = if (longres.toFloat() == number) MIN_DECIMAL_PLACES else decimalPlaces

        return number
            .toBigDecimal()
            .setScale(digits, RoundingMode.HALF_UP)
    }

    private companion object {
        const val SEPARATOR_COMMA = ","
        const val SEPARATOR_SPACE = " "
        const val PERCENT = "%"
        const val MIN_DECIMAL_PLACES = 0
        const val MIN_MONEY_DECIMAL_PLACES = 0
        const val PERCENTAGE_DECIMAL_PLACES = 1
    }
}
