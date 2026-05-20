package com.example.harmoney.domain.models

import com.example.harmoney.base.IdEnum
import com.example.harmoney.base.IdEnumRegistry

enum class StatisticsPeriod(override val id: Long) : IdEnum {
    LAST_MONTH(id = 1),
    CURRENT_MONTH(id = 2);

    companion object {
        private val registry = IdEnumRegistry(StatisticsPeriod::class.java) { it.id }
        fun fromId(id: Long) = registry.fromId(id)
    }
}
