package com.example.harmoney.presentation.models

import com.example.harmoney.domain.models.StatisticsPeriodType

data class StatisticsPeriodUi(
    val type: StatisticsPeriodType,
    val date: String,
)
