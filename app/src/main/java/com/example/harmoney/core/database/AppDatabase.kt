package com.example.harmoney.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.harmoney.data.account.dao.AccountDao
import com.example.harmoney.data.account.entity.AccountEntity
import com.example.harmoney.data.category.dao.CategoryDao
import com.example.harmoney.data.category.entity.CategoryEntity
import com.example.harmoney.data.core.bd.dto.TransactionDetailsDto
import com.example.harmoney.data.currencyList.db.dao.CurrencyListDao
import com.example.harmoney.data.currencyList.db.entity.CurrencyEntity
import com.example.harmoney.data.statistics.dao.StatisticsDao
import com.example.harmoney.data.transaction.dao.TransactionDao
import com.example.harmoney.data.transaction.entity.TransactionEntity

@Database(
    entities = [CategoryEntity::class, TransactionEntity::class, CurrencyEntity::class, AccountEntity::class],
    views = [TransactionDetailsDto::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao

    abstract fun transactionDao(): TransactionDao

    abstract fun currencyListDao(): CurrencyListDao

    abstract fun accountDao(): AccountDao

    abstract fun statisticsDao(): StatisticsDao
}
