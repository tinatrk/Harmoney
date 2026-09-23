package com.example.harmoney.domain.models

import com.example.harmoney.core.util.IdEnum
import com.example.harmoney.core.util.IdEnumRegistry

enum class StatisticsPeriodType(override val id: Long) : IdEnum {
    LAST_MONTH(id = 1),
    CURRENT_MONTH(id = 2);

    companion object {
        private val registry = IdEnumRegistry(StatisticsPeriodType::class.java)
        /** Throws `IllegalStateException` if id is unknown */
        fun fromId(id: Long) = registry.fromId(id)
    }
}
