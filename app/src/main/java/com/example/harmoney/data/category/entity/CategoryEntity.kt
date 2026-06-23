package com.example.harmoney.data.category.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "typeId") val typeId: Long,
    @ColumnInfo(name = "iconId") val iconId: Long,
    @ColumnInfo(name = "iconColorId") val iconColorId: Long,
    @ColumnInfo(name = "createdAt") val createdAt: Long,
    @ColumnInfo(name = "userOrder") val userOrder: Double
)
