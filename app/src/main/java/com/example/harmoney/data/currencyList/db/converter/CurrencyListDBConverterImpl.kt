package com.example.harmoney.data.currencyList.db.converter

import com.example.harmoney.data.currencyList.db.entity.CurrencyEntity
import com.example.harmoney.domain.models.Currency

class CurrencyListDBConverterImpl : CurrencyListDBConverter {
    override fun map(currency: CurrencyEntity): Currency {
        // Если не найдем Currency с таким isoCode, выпадет исключение
        return Currency.fromIsoCode(currency.isoCode)
    }

    override fun map(currencyList: List<CurrencyEntity>): List<Currency> {
        return currencyList.map { this.map(it) }
    }
}
