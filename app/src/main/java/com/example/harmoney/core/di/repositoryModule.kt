package com.example.harmoney.core.di

import com.example.harmoney.data.account.impl.AccountRepositoryImpl
import com.example.harmoney.data.account.impl.SelectedAccountScopeRepositoryImpl
import com.example.harmoney.data.category.impl.CategoryRepositoryImpl
import com.example.harmoney.data.currencyList.CurrencyListRepositoryImpl
import com.example.harmoney.data.exchangeRate.ExchangeRateRepositoryImpl
import com.example.harmoney.data.settings.categorySortingMode.impl.CategorySortOptionRepositoryImpl
import com.example.harmoney.data.settings.firstDayMonth.impl.FirstDayMonthRepositoryImpl
import com.example.harmoney.data.settings.reportingCurrency.impl.ReportingCurrencyRepositoryImpl
import com.example.harmoney.data.settings.theme.impl.ThemeRepositoryImpl
import com.example.harmoney.data.statistics.impl.StatisticsRepositoryImpl
import com.example.harmoney.data.transaction.impl.TransactionRepositoryImpl
import com.example.harmoney.domain.category.api.reposiory.CategoryRepository
import com.example.harmoney.domain.currencyList.api.repository.CurrencyListRepository
import com.example.harmoney.domain.settings.categorySortingMode.api.repository.CategorySortOptionRepository
import com.example.harmoney.domain.settings.currency.api.repository.ReportingCurrencyRepository
import com.example.harmoney.domain.settings.period.api.repository.FirstDayMonthRepository
import com.example.harmoney.domain.settings.theme.api.repository.ThemeRepository
import com.example.harmoney.domain.transaction.api.repository.TransactionRepository
import org.koin.dsl.module
import com.example.harmoney.domain.account.api.repository.AccountRepository
import com.example.harmoney.domain.account.api.repository.SelectedAccountScopeRepository
import com.example.harmoney.domain.exchangeRate.api.repository.ExchangeRateRepository
import com.example.harmoney.domain.statistics.api.repository.StatisticsRepository

val repositoryModule = module {
    factory<ThemeRepository> {
        ThemeRepositoryImpl(dataStore = get())
    }

    factory<FirstDayMonthRepository> {
        FirstDayMonthRepositoryImpl(dataStore = get())
    }

    factory<CategorySortOptionRepository> {
        CategorySortOptionRepositoryImpl(dataStore = get())
    }

    factory<CategoryRepository> {
        CategoryRepositoryImpl(categoryDao = get(), categoryDBConverter = get())
    }

    factory<TransactionRepository> {
        TransactionRepositoryImpl(
            transactionDao = get(),
            transactionDbConverter = get(),
            dateConverter = get(),
            statisticsDao = get(),
        )
    }

    factory<ReportingCurrencyRepository> {
        ReportingCurrencyRepositoryImpl(dataStore = get())
    }

    factory<CurrencyListRepository> {
        CurrencyListRepositoryImpl(
            currencyListDao = get(),
            remoteDataSource = get(),
            currencyListDBConverter = get(),
            currencyListDtoConverter = get()
        )
    }

    factory<AccountRepository> {
        AccountRepositoryImpl(accountDao = get(), accountDBConverter = get())
    }

    factory<SelectedAccountScopeRepository> {
        SelectedAccountScopeRepositoryImpl(dataStore = get())
    }

    factory<StatisticsRepository> {
        StatisticsRepositoryImpl(
            statisticsDao = get(),
            dateConverter = get(),
            categoryStatisticsDbConverter = get()
        )
    }

    factory<ExchangeRateRepository> {
        ExchangeRateRepositoryImpl(
            remoteDataSource = get(),
            exchangeRateDao = get(),
            exchangeRateDBConverter = get(),
            exchangeRateDtoConverter = get()
        )
    }
}
