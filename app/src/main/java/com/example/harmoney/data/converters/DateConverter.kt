package com.example.harmoney.data.converters

import android.util.Log
import java.time.DateTimeException
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class DateConverter {
    fun millisToDate(dateMillis: Long): LocalDate {
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

    fun dateToMillis(date: LocalDate): Long {
        return date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }

    private companion object {
        const val TAG = "HarmAppDateFormatterImpl"
    }
}
