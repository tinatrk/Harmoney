package com.example.harmoney.domain.models

import com.example.harmoney.core.util.IdEnum
import com.example.harmoney.core.util.IdEnumRegistry

enum class SortOption(override val id: Long) : IdEnum {
    TIME_CREATED(id = 1),
    ALPHABET(id = 2),
    USER_ORDER(id = 3);

    companion object {
        private val registry = IdEnumRegistry(SortOption::class.java)
        /** Throws `IllegalStateException` if id is unknown */
        fun fromId(id: Long) = registry.fromId(id)
    }
}
