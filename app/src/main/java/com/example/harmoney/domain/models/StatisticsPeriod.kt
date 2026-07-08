package com.example.harmoney.domain.models

import java.time.LocalDate

data class StatisticsPeriod(
    val type: StatisticsPeriodType,
    val firstDay: LocalDate,
    val lastDay: LocalDate,
)
