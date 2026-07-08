package com.example.harmoney.presentation.converters

import com.example.harmoney.domain.models.StatisticsPeriod
import com.example.harmoney.presentation.models.StatisticsPeriodUi

class StatisticsPeriodUiConverterImpl(
    private val dateFormatter: DateFormatter
) : StatisticsPeriodUiConverter {
    override fun map(period: StatisticsPeriod): StatisticsPeriodUi {
        return StatisticsPeriodUi(
            type = period.type,
            date = dateFormatter.formatPeriod(period.firstDay, period.lastDay)
        )
    }
}
