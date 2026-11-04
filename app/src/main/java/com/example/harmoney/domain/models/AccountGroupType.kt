package com.example.harmoney.domain.models

import com.example.harmoney.core.util.IdEnum
import com.example.harmoney.core.util.IdEnumRegistry

enum class AccountGroupType(override val id: Long) : IdEnum {
    //Здесь используются отрицательные значения, чтобы id точно не совпали с id счетов
    NOT_ARCHIVED(id = -1L),
    ALL(id = -2L);

    companion object {
        private val registry = IdEnumRegistry(AccountGroupType::class.java)

        /** Throws `IllegalStateException` if id is unknown */
        fun fromId(id: Long) = registry.fromId(id)
    }
}
