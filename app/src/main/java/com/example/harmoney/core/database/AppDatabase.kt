package com.example.harmoney.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.harmoney.data.category.dao.CategoryDao
import com.example.harmoney.data.category.entity.CategoryEntity
import com.example.harmoney.data.core.TransactionWithCategory
import com.example.harmoney.data.transaction.dao.TransactionDao
import com.example.harmoney.data.transaction.entity.TransactionEntity

@Database(
    entities = [CategoryEntity::class, TransactionEntity::class],
    views = [TransactionWithCategory::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao

    abstract fun transactionDao(): TransactionDao
}
