package com.example.harmoney.data.exchangeRate.db.converter

import com.example.harmoney.data.exchangeRate.db.entity.ExchangeRateEntity
import com.example.harmoney.domain.models.ExchangeRate

interface ExchangeRateDBConverter {
    fun map(exchangeRate: ExchangeRate): ExchangeRateEntity

    fun map(exchangeRate: ExchangeRateEntity): ExchangeRate

    fun map(rates: List<ExchangeRateEntity>): List<ExchangeRate>
}
