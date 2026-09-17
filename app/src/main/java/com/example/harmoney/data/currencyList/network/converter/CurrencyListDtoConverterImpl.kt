package com.example.harmoney.data.currencyList.network.converter

import com.example.harmoney.data.currencyList.db.entity.CurrencyEntity
import com.example.harmoney.data.currencyList.network.dto.CurrencyDto
import com.example.harmoney.domain.models.Currency
import java.time.LocalDate

class CurrencyListDtoConverterImpl : CurrencyListDtoConverter {
    override fun map(currency: CurrencyDto): Currency? {
        return Currency.fromIsoCodeOrNull(currency.isoCode)
    }

    override fun mapToEntity(currency: CurrencyDto): CurrencyEntity? {
        if (currency.startDate.isNullOrEmpty()
            || Currency.fromIsoCodeOrNull(currency.isoCode) == null
        ) return null

        return CurrencyEntity(
            isoCode = currency.isoCode,
            startDateEpochDay = currency.startDate.let(LocalDate::parse).toEpochDay(),
            endDateEpochDay = currency.endDate?.let(LocalDate::parse)?.toEpochDay(),
        )
    }

    override fun map(currencies: List<CurrencyDto>): List<Currency> {
        return currencies.mapNotNull { this.map(it) }
    }

    override fun mapToEntity(currencyList: List<CurrencyDto>): List<CurrencyEntity> {
        return currencyList.mapNotNull { this.mapToEntity(it) }
    }
}
