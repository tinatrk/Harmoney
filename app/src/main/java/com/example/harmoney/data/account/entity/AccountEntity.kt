package com.example.harmoney.data.account.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "account"
)
data class AccountEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Long = 0L,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "currencyId") val currencyId: Long,
    @ColumnInfo(name = "initialBalanceMinorUnits") val initialBalanceMinorUnits: Long,
    @ColumnInfo(name = "isArchived") val isArchived: Boolean,
    @ColumnInfo(name = "createdAtMillis")val createdAtMillis: Long,
    @ColumnInfo(name = "userOrder") val userOrder: Double = 0.0,
)
