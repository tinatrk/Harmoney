package com.example.harmoney.data.transaction.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.harmoney.data.account.entity.AccountEntity
import com.example.harmoney.data.category.entity.CategoryEntity

@Entity(
    tableName = "transaction",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = AccountEntity::class,
            parentColumns = ["id"],
            childColumns = ["accountId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["categoryId"]), Index(value = ["accountId"])]
)
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "accountId") val accountId: Long,
    @ColumnInfo(name = "categoryId") val categoryId: Long,
    @ColumnInfo(name = "dateMillis") val dateMillis: Long,
    @ColumnInfo(name = "moneyAmountMinorUnits") val moneyAmountMinorUnits: Long,
    @ColumnInfo(name = "note") val note: String,
    @ColumnInfo(name = "createdAt") val createdAt: Long
)
