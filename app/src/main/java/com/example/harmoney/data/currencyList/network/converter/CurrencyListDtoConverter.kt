package com.example.harmoney.data.currencyList.network.converter

import com.example.harmoney.data.currencyList.db.entity.CurrencyEntity
import com.example.harmoney.data.currencyList.network.dto.CurrencyDto
import com.example.harmoney.domain.models.Currency

interface CurrencyListDtoConverter {
    fun map(currency: CurrencyDto): Currency?

    fun mapToEntity(currency: CurrencyDto): CurrencyEntity?

    fun map(currencies: List<CurrencyDto>): List<Currency>

    fun mapToEntity(currencyList: List<CurrencyDto>): List<CurrencyEntity>
}
