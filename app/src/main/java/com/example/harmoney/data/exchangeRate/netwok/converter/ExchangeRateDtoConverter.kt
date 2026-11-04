package com.example.harmoney.data.exchangeRate.netwok.converter

import com.example.harmoney.data.exchangeRate.db.entity.ExchangeRateEntity
import com.example.harmoney.data.exchangeRate.netwok.dto.ExchangeRateDto

interface ExchangeRateDtoConverter {
    fun map(rate: ExchangeRateDto): ExchangeRateEntity?

    fun map(rates: List<ExchangeRateDto>): List<ExchangeRateEntity>
}
