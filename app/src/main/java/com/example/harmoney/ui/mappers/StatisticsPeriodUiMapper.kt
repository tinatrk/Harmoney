package com.example.harmoney.ui.mappers

import com.example.harmoney.R
import com.example.harmoney.domain.models.StatisticsPeriodType

object StatisticsPeriodUiMapper {
    fun StatisticsPeriodType.toStringRes(): Int =
        when (this) {
            StatisticsPeriodType.LAST_MONTH -> R.string.period_last_month
            StatisticsPeriodType.CURRENT_MONTH -> R.string.period_current_month
        }
}

