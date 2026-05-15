package com.example.harmoney.domain.models

import androidx.annotation.StringRes
import com.example.harmoney.R
import com.example.harmoney.base.IdEnum
import com.example.harmoney.base.IdEnumRegistry

enum class StatisticPeriod(override val id: Long, @StringRes val textRes: Int) : IdEnum {
    LAST_MONTH(id = 1, R.string.period_last_month),
    CURRENT_MONTH(id = 2, R.string.period_current_month);

    companion object {
        private val registry = IdEnumRegistry(StatisticPeriod::class.java) { it.id }
        fun fromId(id: Long) = registry.fromId(id)
    }
}