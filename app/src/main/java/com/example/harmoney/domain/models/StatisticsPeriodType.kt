package com.example.harmoney.domain.models

import com.example.harmoney.base.IdEnum
import com.example.harmoney.base.IdEnumRegistry

enum class StatisticsPeriodType(override val id: Long) : IdEnum {
    LAST_MONTH(id = 1),
    CURRENT_MONTH(id = 2);

    companion object {
        private val registry = IdEnumRegistry(StatisticsPeriodType::class.java) { it.id }
        fun fromId(id: Long) = registry.fromId(id)
    }
}
