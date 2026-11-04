package com.example.harmoney.data.currencyList.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "supported_currencies")
data class CurrencyEntity(
    @PrimaryKey
    @ColumnInfo(name = "isoCode") val isoCode: String,
    @ColumnInfo(name = "startDateEpochDay") val startDateEpochDay: Long,
    @ColumnInfo(name = "endDateEpochDay") val endDateEpochDay: Long?,
)
