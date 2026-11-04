package com.example.harmoney.core.util

class IdEnumRegistry<T>(
    private val enumClass: Class<T>,
) where T : Enum<T>, T : IdEnum {

    private val byId: Map<Long, T> = buildMap {
        for (value in enumClass.enumConstants ?: emptyArray()) {
            val previous = put(value.id, value)

            check(previous == null) {
                "Duplicate id '${value.id}' in enum ${enumClass.simpleName}: " +
                        "'${previous?.name}' and '${value.name}'"
            }
        }
    }

    /** Throws `IllegalStateException` if id is unknown */
    fun fromId(id: Long): T =
        byId[id] ?: error("Unknown ${enumClass.simpleName} id: $id")
}
