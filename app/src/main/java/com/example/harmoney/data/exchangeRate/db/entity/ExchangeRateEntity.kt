package com.example.harmoney.data.exchangeRate.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.math.BigDecimal

@Entity(
    tableName = "exchange_rates",
    primaryKeys = [
        "baseCurrencyId",
        "quoteCurrencyId",
        "dateEpochDay"
    ],
)
data class ExchangeRateEntity(
    @ColumnInfo(name = "baseCurrencyId") val baseCurrencyId: Long,
    @ColumnInfo(name = "quoteCurrencyId") val quoteCurrencyId: Long,
    @ColumnInfo(name = "dateEpochDay") val dateEpochDay: Long,
    @ColumnInfo(name = "rateFromBaseToQuote") val rateFromBaseToQuote: BigDecimal
)
