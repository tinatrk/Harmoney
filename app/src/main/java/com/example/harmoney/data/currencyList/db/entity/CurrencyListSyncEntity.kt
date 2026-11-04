package com.example.harmoney.data.currencyList.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currency_list_sync")
data class CurrencyListSyncEntity(
    @PrimaryKey
    val id: Long = 1L,
    val lastSuccessfulSyncEpochDay: Long,
)
