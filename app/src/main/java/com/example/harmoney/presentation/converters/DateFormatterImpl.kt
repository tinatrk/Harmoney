package com.example.harmoney.presentation.converters

import android.util.Log
import com.example.harmoney.presentation.models.DatePattern
import java.time.DateTimeException
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DateFormatterImpl : DateFormatter {
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
