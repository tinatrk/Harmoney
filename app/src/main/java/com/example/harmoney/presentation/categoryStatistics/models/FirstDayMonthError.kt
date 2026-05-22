package com.example.harmoney.presentation.categoryStatistics.models

sealed interface FirstDayMonthError {
    data object None: FirstDayMonthError
    data class OutOfRange(val minDay: Int, val maxDay: Int) : FirstDayMonthError
}
