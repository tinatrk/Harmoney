package com.example.harmoney.presentation.converters

import com.example.harmoney.domain.models.Currency
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
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

    private fun toStringWithThousandSeparator(number: Float): String {
        val decimalFormatter = DecimalFormat(
            DECIMAL_FORMAT_PATTERN,
            DecimalFormatSymbols(Locale.GERMANY)
        )
        return decimalFormatter.format(number)
    }

    private fun toStringWithCurrency(number: String, currency: Currency): String {
        return String.format("%s ${currency.symbol}", number)
    }

    override fun toStringWithPercent(number: Double, decimalPlaces: Int): String {
        val roundedNumber = roundNumber(number, decimalPlaces)
        // Используется не String.format, чтобы сохранить округление
        return "${roundedNumber}$PERCENT"
    }

    private fun roundNumber(number: Double, decimalPlaces: Int): Float {
        val longres = number.toLong()
        val digits = when {
            // Если дробная часть равна нулю, то она будет отброшена
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

        return res.toFloat()
    }

    private companion object {
        const val DECIMAL_FORMAT_PATTERN = "#,###"
        const val PERCENT = "%"
        const val MIN_DECIMAL_PLACES = 0
        const val MAX_DECIMAL_PLACES = 6
    }
}
