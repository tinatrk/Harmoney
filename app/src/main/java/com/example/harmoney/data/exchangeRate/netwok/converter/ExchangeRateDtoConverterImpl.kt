package com.example.harmoney.data.exchangeRate.netwok.converter

import com.example.harmoney.data.exchangeRate.db.entity.ExchangeRateEntity
import com.example.harmoney.data.exchangeRate.netwok.dto.ExchangeRateDto
import com.example.harmoney.domain.models.Currency
import java.time.LocalDate

class ExchangeRateDtoConverterImpl : ExchangeRateDtoConverter {
    override fun map(rate: ExchangeRateDto): ExchangeRateEntity? {
        val baseCurrency = Currency.fromIsoCodeOrNull(rate.baseIsoCode)
        val quoteCurrency = Currency.fromIsoCodeOrNull(rate.quoteIsoCode)

        if (baseCurrency == null || quoteCurrency == null) return null

        return ExchangeRateEntity(
            baseCurrencyId = baseCurrency.id,
            quoteCurrencyId = quoteCurrency.id,
            dateEpochDay = LocalDate.parse(rate.date).toEpochDay(),
            rateFromBaseToQuote = rate.rate
        )
    }

    override fun map(rates: List<ExchangeRateDto>): List<ExchangeRateEntity> {
        return rates.mapNotNull { map(it) }
    }
}
