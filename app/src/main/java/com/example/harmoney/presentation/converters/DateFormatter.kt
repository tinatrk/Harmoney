package com.example.harmoney.presentation.converters

import java.time.LocalDate

interface DateFormatter {
    fun formatDate(date: LocalDate): String

    fun formatShortDate(date: LocalDate): String

    fun formatPeriod(first: LocalDate, last: LocalDate): String
}
