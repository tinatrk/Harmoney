package com.example.harmoney.presentation.converters

import java.time.LocalDate

interface DateFormatter {
    //fun dateToMillis(date: LocalDate): Long

    //fun millisToDate(dateMillis: Long): LocalDate

    //fun dateToString(date: LocalDate, pattern: String): String

    //fun stringToDate(date: String, pattern: String): LocalDate?

    //fun millisToString(dateMillis: Long, pattern: String): String

    //fun stringToMillis(date: String, pattern: String): Long?

    /*fun datesMillisToPeriodString(
        firstDateMillis: Long,
        lastDayMillis: Long,
        pattern: String
    ): String*/

    fun formatDate(date: LocalDate): String

    fun formatShortDate(date: LocalDate): String

    fun formatPeriod(first: LocalDate, last: LocalDate): String
}
