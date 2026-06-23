package com.example.harmoney.core.di

import androidx.room.Room
import com.example.harmoney.core.database.AppDatabase
import com.example.harmoney.data.category.dao.CategoryDao
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "harm_database"
        ).build()
    }

    factory<CategoryDao> {
        val database = get<AppDatabase>()
        database.categoryDao()
    }
}
