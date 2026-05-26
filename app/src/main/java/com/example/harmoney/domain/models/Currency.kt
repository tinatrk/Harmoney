package com.example.harmoney.domain.models

import com.example.harmoney.base.IdEnum
import com.example.harmoney.base.IdEnumRegistry

enum class Currency(override val id: Long, val code: String, val symbol: String) : IdEnum {
    EUR(id = 1, code = CurrencyStrings.EUR_CODE, symbol = CurrencyStrings.EUR_SYMBOL),
    RUB(id = 2, code = CurrencyStrings.RUB_CODE, symbol = CurrencyStrings.RUB_SYMBOL),
    USD(id = 3, code = CurrencyStrings.USD_CODE, symbol = CurrencyStrings.USD_SYMBOL);


    companion object {
        private val registry = IdEnumRegistry(Currency::class.java) { it.id }
        fun fromId(id: Long) = registry.fromId(id)
    }
}

private object CurrencyStrings {
    const val EUR_CODE = "EUR"
    const val EUR_SYMBOL = "€"
    const val RUB_CODE = "RUB"
    const val RUB_SYMBOL = "₽"
    const val USD_CODE = "USD"
    const val USD_SYMBOL = "$"
}
