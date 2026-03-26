package com.example.harmoney.base

class IdEnumRegistry<T>(
    private val enumClass: Class<T>,
    private val idSelector: (T) -> Long
) where T : Enum<T>, T : IdEnum {

    private val byId: Map<Long, T>
    private val usedIds = mutableSetOf<Long>()

    init {
        val constants = enumClass.enumConstants ?: emptyArray()

        byId = constants.associateBy { value ->
            val id = idSelector(value)
            require(usedIds.add(id)) {
                "Duplicate id '$id' in enum ${enumClass.simpleName}: " +
                        "element '${(value as Enum<*>).name}'"
            }
            id
        }
    }

    fun fromId(id: Long): T =
        byId[id] ?: error("Unknown ${byId.values.first()::class.simpleName} id: $id")
}
