package com.example.harmoney.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.room.Room
import com.example.harmoney.core.database.AppDatabase
import com.example.harmoney.data.category.dao.CategoryDao
import com.example.harmoney.data.transaction.dao.TransactionDao
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import java.io.File

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

    factory<TransactionDao> {
        val database = get<AppDatabase>()
        database.transactionDao()
    }

    single<DataStore<Preferences>> {
        PreferenceDataStoreFactory.create(
            produceFile = {
                File(get<Context>().filesDir, "settings.preferences_pb")
            }
        )
    }
}
