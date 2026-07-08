package com.example.harmoney.core.di

import com.example.harmoney.data.settings.firstDayMonth.impl.FirstDayMonthRepositoryImpl
import com.example.harmoney.data.settings.theme.impl.ThemeRepositoryImpl
import com.example.harmoney.domain.settings.period.api.repository.FirstDayMonthRepository
import com.example.harmoney.domain.settings.theme.api.repository.ThemeRepository
import org.koin.dsl.module

val repositoryModule = module {
    factory<ThemeRepository> {
        ThemeRepositoryImpl(dataStore = get())
    }

    factory<FirstDayMonthRepository> {
        FirstDayMonthRepositoryImpl(dataStore = get())
    }

    factory<FirstDayMonthRepository> {
        FirstDayMonthRepositoryImpl(dataStore = get())
    }
}
