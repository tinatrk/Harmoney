package com.example.harmoney.ui.mappers

import com.example.harmoney.R
import com.example.harmoney.domain.models.StatisticsPeriod

object StatisticsPeriodUiMapper {
    fun StatisticsPeriod.toStringRes(): Int =
        when (this) {
            StatisticsPeriod.LAST_MONTH -> R.string.period_last_month
            StatisticsPeriod.CURRENT_MONTH -> R.string.period_current_month
        }
}