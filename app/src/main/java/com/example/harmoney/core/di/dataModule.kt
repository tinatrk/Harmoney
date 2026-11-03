package com.example.harmoney.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.room.Room
import com.example.harmoney.core.database.AppDatabase
import com.example.harmoney.data.account.converter.AccountDBConverter
import com.example.harmoney.data.account.converter.AccountDBConverterImpl
import com.example.harmoney.data.account.dao.AccountDao
import com.example.harmoney.data.category.converter.CategoryDBConverter
import com.example.harmoney.data.category.converter.CategoryDBConverterImpl
import com.example.harmoney.data.category.dao.CategoryDao
import com.example.harmoney.data.converters.DateConverter
import com.example.harmoney.data.core.bd.converter.TotalMoneyDtoConverter
import com.example.harmoney.data.core.bd.converter.TotalMoneyDtoConverterImpl
import com.example.harmoney.data.currencyList.db.converter.CurrencyListDBConverter
import com.example.harmoney.data.currencyList.db.converter.CurrencyListDBConverterImpl
import com.example.harmoney.data.currencyList.db.dao.CurrencyListDao
import com.example.harmoney.data.currencyList.network.SupportedCurrenciesDataSource
import com.example.harmoney.data.currencyList.network.SupportedCurrenciesDataSourceImpl
import com.example.harmoney.data.currencyList.network.converter.CurrencyListDtoConverter
import com.example.harmoney.data.currencyList.network.converter.CurrencyListDtoConverterImpl
import com.example.harmoney.data.exchangeRate.ExchangeRateProvider
import com.example.harmoney.data.exchangeRate.ExchangeRateProviderImpl
import com.example.harmoney.data.exchangeRate.db.converter.ExchangeRateDBConverter
import com.example.harmoney.data.exchangeRate.db.converter.ExchangeRateDBConverterImpl
import com.example.harmoney.data.exchangeRate.db.dao.ExchangeRateDao
import com.example.harmoney.data.exchangeRate.netwok.converter.ExchangeRateDtoConverter
import com.example.harmoney.data.exchangeRate.netwok.converter.ExchangeRateDtoConverterImpl
import com.example.harmoney.data.network.FrankfurterApi
import com.example.harmoney.data.network.FrankfurterRemoteDataSourceImpl
import com.example.harmoney.data.network.RemoteDataSource
import com.example.harmoney.data.statistics.converter.CategoryStatisticsDbConverter
import com.example.harmoney.data.statistics.converter.CategoryStatisticsDbConverterImpl
import com.example.harmoney.data.statistics.dao.StatisticsDao
import com.example.harmoney.data.transaction.converter.TransactionDBConverter
import com.example.harmoney.data.transaction.converter.TransactionDBConverterImpl
import com.example.harmoney.data.transaction.dao.TransactionDao
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.io.File

private const val BASE_URL_API = "https://api.frankfurter.dev/v2/"
private const val SHARED_PREFERENCES_FILE = "settings.preferences_pb"
private const val DATABASE_NAME = "harm_database"

val dataModule = module {
    // region core
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    factory {
        DateConverter()
    }
    // endregion


    // region category
    factory<CategoryDao> {
        val database = get<AppDatabase>()
        database.categoryDao()
    }

    factory<CategoryDBConverter> {
        CategoryDBConverterImpl()
    }
    // endregion


    // region transaction
    factory<TransactionDao> {
        val database = get<AppDatabase>()
        database.transactionDao()
    }

    factory<TransactionDBConverter> {
        TransactionDBConverterImpl(dateConverter = get(), categoryDBConverter = get())
    }
    // endregion


    // region settings
    single<DataStore<Preferences>> {
        PreferenceDataStoreFactory.create(
            produceFile = {
                File(get<Context>().filesDir, SHARED_PREFERENCES_FILE)
            }
        )
    }
    // endregion


    // region network
    single<FrankfurterApi> {
        val json = Json {
            ignoreUnknownKeys = true
        }

        Retrofit.Builder()
            .baseUrl(BASE_URL_API)
            .addConverterFactory(
                json.asConverterFactory(MediaType.get("application/json"))
            )
            .build()
            .create(FrankfurterApi::class.java)
    }

    single<RemoteDataSource> {
        FrankfurterRemoteDataSourceImpl(frankfurterApi = get())
    }
    // endregion


    // region currency list
    factory<CurrencyListDao> {
        val database = get<AppDatabase>()
        database.currencyListDao()
    }

    factory<CurrencyListDBConverter> {
        CurrencyListDBConverterImpl()
    }

    factory<CurrencyListDtoConverter> {
        CurrencyListDtoConverterImpl()
    }

    // single т.к внутри используется Mutex
    single<SupportedCurrenciesDataSource> {
        SupportedCurrenciesDataSourceImpl(
            database = get<AppDatabase>(),
            currencyListDao = get(),
            remoteDataSource = get(),
            dbConverter = get(),
            dtoConverter = get()
        )
    }
    // endregion


    // region account
    factory<AccountDao> {
        val database = get<AppDatabase>()
        database.accountDao()
    }

    factory<AccountDBConverter> {
        AccountDBConverterImpl()
    }
    // endregion


    // region statistics
    factory<StatisticsDao> {
        val database = get<AppDatabase>()
        database.statisticsDao()
    }

    factory<CategoryStatisticsDbConverter> {
        CategoryStatisticsDbConverterImpl(totalMoneyDtoConverter = get())
    }

    factory<TotalMoneyDtoConverter> {
        TotalMoneyDtoConverterImpl()
    }
    // endregion

    // region exchange rate
    factory<ExchangeRateDao> {
        val database = get<AppDatabase>()
        database.exchangeRateDao()
    }

    factory<ExchangeRateDBConverter> {
        ExchangeRateDBConverterImpl()
    }

    factory<ExchangeRateDtoConverter> {
        ExchangeRateDtoConverterImpl()
    }

    // single т.к. внутри используется Mutex
    single<ExchangeRateProvider> {
        ExchangeRateProviderImpl(
            remoteDataSource = get(),
            exchangeRateDao = get(),
            exchangeRateDBConverter = get(),
            exchangeRateDtoConverter = get(),
            supportedCurrenciesDataSource = get()
        )
    }
    // endregion
}
