package com.example.harmoney.data.currencyList.db.converter

import com.example.harmoney.data.currencyList.db.entity.CurrencyEntity
import com.example.harmoney.data.currencyList.network.dto.CurrencyDto
import com.example.harmoney.domain.models.Currency

interface CurrencyListDBConverter {
    fun map(currency: CurrencyEntity): Currency

    fun map(currencyList: List<CurrencyEntity>): List<Currency>
}
