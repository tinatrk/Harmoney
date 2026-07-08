package com.example.harmoney.presentation.converters

import com.example.harmoney.domain.models.StatisticsPeriod
import com.example.harmoney.presentation.models.StatisticsPeriodUi

interface StatisticsPeriodUiConverter {
    fun map(period: StatisticsPeriod) : StatisticsPeriodUi
}
