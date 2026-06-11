package com.example.harmoney.presentation.converters

import java.time.LocalDate

interface DateFormatter {
    fun dateToMillis(date: LocalDate): Long

    fun millisToDate(dateMillis: Long): LocalDate

    fun dateToString(date: LocalDate, pattern: String): String

    fun millisToString(dateMillis: Long, pattern: String): String
}
