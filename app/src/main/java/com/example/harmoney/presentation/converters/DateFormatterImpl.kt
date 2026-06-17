package com.example.harmoney.presentation.converters

import android.util.Log
import java.time.DateTimeException
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class DateFormatterImpl : DateFormatter {
    override fun dateToMillis(date: LocalDate): Long {
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

    override fun millisToString(dateMillis: Long, pattern: String): String {
        return dateToString(date = millisToDate(dateMillis), pattern = pattern)
    }

    private companion object {
        const val TAG = "HarmAppDateFormatterImpl"
    }
}
