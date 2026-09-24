package com.example.harmoney.data.exchangeRate.db.entity

import androidx.room.Entity

@Entity(
    tableName = "exchange_rate_sync",
    primaryKeys = [
        "baseCurrencyId",
        "quoteCurrencyId",
        "dateEpochDay",
    ],
)
data class ExchangeRateSyncEntity(
    val baseCurrencyId: Long,
    val quoteCurrencyId: Long,
    val dateEpochDay: Long,
)
