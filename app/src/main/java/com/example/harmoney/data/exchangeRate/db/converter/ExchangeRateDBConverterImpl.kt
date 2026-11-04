package com.example.harmoney.data.exchangeRate.db.converter

import com.example.harmoney.data.exchangeRate.db.entity.ExchangeRateEntity
import com.example.harmoney.domain.models.Currency
import com.example.harmoney.domain.models.ExchangeRate
import java.time.LocalDate

class ExchangeRateDBConverterImpl : ExchangeRateDBConverter {
    override fun map(exchangeRate: ExchangeRate): ExchangeRateEntity {
        return ExchangeRateEntity(
            baseCurrencyId = exchangeRate.sourceCurrency.id,
            quoteCurrencyId = exchangeRate.targetCurrency.id,
            dateEpochDay = exchangeRate.rateDate.toEpochDay(),
            rateFromBaseToQuote = exchangeRate.rateValue
        )
    }

    override fun map(exchangeRate: ExchangeRateEntity): ExchangeRate {
        return ExchangeRate(
            sourceCurrency = Currency.fromId(exchangeRate.baseCurrencyId),
            targetCurrency = Currency.fromId(exchangeRate.quoteCurrencyId),
            rateValue = exchangeRate.rateFromBaseToQuote,
            rateDate = LocalDate.ofEpochDay(exchangeRate.dateEpochDay),
        )
    }

    override fun map(rates: List<ExchangeRateEntity>): List<ExchangeRate> {
        return rates.map { map(it) }
    }
}
