package com.example.harmoney.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.harmoney.data.category.dao.CategoryDao
import com.example.harmoney.data.category.entity.CategoryEntity

@Database(
    entities = [CategoryEntity::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
}
