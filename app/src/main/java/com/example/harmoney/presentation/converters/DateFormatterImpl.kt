package com.example.harmoney.presentation.converters

import android.util.Log
import com.example.harmoney.presentation.models.DatePattern
import java.time.DateTimeException
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class DateFormatterImpl : DateFormatter {
    /*override fun dateToMillis(date: LocalDate): Long {
        return date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }

    override fun millisToDate(dateMillis: Long): LocalDate {
        return try {
            Instant
                .ofEpochMilli(dateMillis)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
        } catch (e: DateTimeException) {
            e.message?.let { Log.e(TAG, it) }
            LocalDate.now()
        }
    }

    override fun dateToString(date: LocalDate, pattern: String): String {
        return try {
            val formatter = DateTimeFormatter.ofPattern(pattern)
            val res = date.format(formatter)
            res
        } catch (e: IllegalArgumentException) {
            e.message?.let { Log.e(TAG, it) }
            "${date.dayOfMonth}.${date.month}.${date.year}"
        } catch (e: DateTimeException) {
            e.message?.let { Log.e(TAG, it) }
            "${date.dayOfMonth}.${date.month}.${date.year}"
        }
    }

    override fun stringToDate(date: String, pattern: String): LocalDate? {
        return try {
            val formatter = DateTimeFormatter.ofPattern(pattern)
            LocalDate.parse(date, formatter)
        } catch (e: IllegalArgumentException) {
            e.message?.let { Log.e(TAG, it) }
            null
        }
    }

    override fun millisToString(dateMillis: Long, pattern: String): String {
        return dateToString(date = millisToDate(dateMillis), pattern = pattern)
    }

    override fun stringToMillis(date: String, pattern: String): Long? {
        val dateLocale = stringToDate(date, pattern)
        return if (dateLocale != null) {
            dateToMillis(dateLocale)
        } else {
            null
        }
    }

    override fun datesMillisToPeriodString(
        firstDateMillis: Long,
        lastDayMillis: Long,
        pattern: String
    ): String {
        val firstDate = millisToString(firstDateMillis, pattern)
        val lastDate = millisToString(lastDayMillis, pattern)
        return firstDate + DELIMITER + lastDate
    }*/

    override fun formatDate(date: LocalDate): String {
        return dateToString(date, DatePattern.CARD_FULLY)
    }

    override fun formatShortDate(date: LocalDate): String {
        return dateToString(date, DatePattern.CARD_SHORT)
    }

    override fun formatPeriod(first: LocalDate, last: LocalDate): String {
        val firstString = dateToString(first, DatePattern.TITLE_FORMAL)
        val lastString = dateToString(last, DatePattern.TITLE_FORMAL)
        return firstString + DELIMITER + lastString
    }

    private fun dateToString(date: LocalDate, pattern: String): String {
        return try {
            val formatter = DateTimeFormatter.ofPattern(pattern)
            val res = date.format(formatter)
            res
        } catch (e: IllegalArgumentException) {
            e.message?.let { Log.e(TAG, it) }
            "${date.dayOfMonth}.${date.month}.${date.year}"
        } catch (e: DateTimeException) {
            e.message?.let { Log.e(TAG, it) }
            "${date.dayOfMonth}.${date.month}.${date.year}"
        }
    }

    private companion object {
        const val TAG = "HarmAppDateFormatterImpl"
        const val DELIMITER = " - "
    }
}
