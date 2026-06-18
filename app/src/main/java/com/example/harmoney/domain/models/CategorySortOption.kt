package com.example.harmoney.domain.models

import com.example.harmoney.base.IdEnum
import com.example.harmoney.base.IdEnumRegistry

enum class CategorySortOption(override val id: Long) : IdEnum {
    TIME_CREATED(id = 1),
    ALPHABET(id = 2),
    USER_ORDER(id = 3);

    companion object {
        private val registry = IdEnumRegistry(CategorySortOption::class.java) { it.id }
        fun fromId(id: Long) = registry.fromId(id)
    }
}
