package com.example.harmoney.presentation.converters

import com.example.harmoney.domain.models.Currency
import java.text.DecimalFormat
import kotlin.math.pow
import kotlin.math.roundToInt

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

    private fun toStringWithThousandSeparator(number: Number): String {
        val decimalFormatter = DecimalFormat(DECIMAL_FORMAT_PATTERN)
        return decimalFormatter
            .format(number)
            .replace(SEPARATOR_COMMA, SEPARATOR_SPACE)
    }

    private fun toStringWithCurrency(number: String, currency: Currency): String {
        return String.format("%s ${currency.symbol}", number)
    }

    override fun toStringWithPercent(number: Double, decimalPlaces: Int): String {
        val roundedNumber = roundNumber(number, decimalPlaces)
        // Используется не String.format, чтобы сохранить округление
        return "${roundedNumber}$PERCENT"
    }

    private fun roundNumber(number: Double, decimalPlaces: Int): Number {
        val longres = number.toLong()
        val digits = when {
            // Если дробная часть равна нулю, то она будет отброшена (поэтому возвращается Number)
            longres.toDouble() == number -> MIN_DECIMAL_PLACES
            decimalPlaces !in MIN_DECIMAL_PLACES..MAX_DECIMAL_PLACES -> MAX_DECIMAL_PLACES
            else -> decimalPlaces
        }

        val res = if (digits == 0) {
            longres
        } else {
            val сoeff = 10f.pow(digits)
            (number * сoeff).roundToInt() / сoeff
        }

        return res
    }

    private companion object {
        const val SEPARATOR_COMMA = ","
        const val SEPARATOR_SPACE = " "
        const val DECIMAL_FORMAT_PATTERN = "#$SEPARATOR_COMMA###"
        const val PERCENT = "%"
        const val MIN_DECIMAL_PLACES = 0
        const val MAX_DECIMAL_PLACES = 6
    }
}
