package com.example.harmoney.presentation.converters

import com.example.harmoney.domain.models.Currency
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.Locale

class NumbersFormatterImpl : NumbersFormatter {

    override fun toStringWithCurrency(
        number: Double,
        decimalPlaces: Int,
        currency: Currency,
        isNeededThousandSeparator: Boolean
    ): String {
        val roundedNumber = roundNumber(number, decimalPlaces)
        val numberWithSeparator = if (isNeededThousandSeparator) {
            toStringWithThousandSeparator(roundedNumber)
        } else {
            roundedNumber.toString()
        }
        return toStringWithCurrency(numberWithSeparator, currency)
    }

    private fun toStringWithThousandSeparator(number: BigDecimal): String {
        val formatter = NumberFormat.getNumberInstance(Locale.US).apply {
            minimumFractionDigits = MIN_DECIMAL_PLACES
            maximumFractionDigits = MAX_DECIMAL_PLACES
            isGroupingUsed = true
        }
        return formatter
            .format(number)
            .replace(oldValue = SEPARATOR_COMMA, newValue = SEPARATOR_SPACE)

    }

    private fun toStringWithCurrency(number: String, currency: Currency): String {
        return String.format(Locale.ENGLISH, "%s ${currency.symbol}", number)
    }

    override fun toString(
        number: Double,
        decimalPlaces: Int,
        isNeededThousandSeparator: Boolean
    ): String {
        val roundedNumber = roundNumber(number, decimalPlaces)
        return if (isNeededThousandSeparator) {
            toStringWithThousandSeparator(roundedNumber)
        } else {
            roundedNumber.toString()
        }
    }

    override fun toStringWithPercent(number: Double, decimalPlaces: Int): String {
        val roundedNumber = roundNumber(number, decimalPlaces)
        // Используется не String.format, чтобы сохранить округление
        return "${roundedNumber}$PERCENT"
    }

    private fun roundNumber(number: Double, decimalPlaces: Int): BigDecimal {
        val longres = number.toLong()
        val digits = if (longres.toDouble() == number) MIN_DECIMAL_PLACES else decimalPlaces

        return number
            .toBigDecimal()
            .setScale(digits, RoundingMode.HALF_UP)
    }

    private companion object {
        const val SEPARATOR_COMMA = ","
        const val SEPARATOR_SPACE = " "
        const val PERCENT = "%"
        const val MIN_DECIMAL_PLACES = 0
        const val MAX_DECIMAL_PLACES = 10
    }
}
