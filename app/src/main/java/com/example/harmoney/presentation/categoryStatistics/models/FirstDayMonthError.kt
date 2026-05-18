package com.example.harmoney.presentation.categoryStatistics.models

sealed class FirstDayMonthError {
    data object None : FirstDayMonthError()
    data class IncorrectInput(val minDay: Int, val maxDay: Int) : FirstDayMonthError()
}
