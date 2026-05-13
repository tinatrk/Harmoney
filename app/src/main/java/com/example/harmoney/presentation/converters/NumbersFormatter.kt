package com.example.harmoney.presentation.converters

import com.example.harmoney.domain.models.Currency

interface NumbersFormatter {
    fun toStringWithCurrency(
        number: Double,
        decimalPlaces: Int,
        currency: Currency,
        isNeededThousandSeparator: Boolean
    ): String

    fun toStringWithPercent(number: Double, decimalPlaces: Int): String
}
