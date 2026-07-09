package com.example.harmoney.core.di

import com.example.harmoney.data.category.impl.CategoryRepositoryImpl
import com.example.harmoney.data.settings.categorySortingMode.impl.CategorySortOptionRepositoryImpl
import com.example.harmoney.data.settings.firstDayMonth.impl.FirstDayMonthRepositoryImpl
import com.example.harmoney.data.settings.theme.impl.ThemeRepositoryImpl
import com.example.harmoney.domain.category.api.reposiory.CategoryRepository
import com.example.harmoney.domain.settings.categorySortingMode.api.repository.CategorySortOptionRepository
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

    factory<CategorySortOptionRepository> {
        CategorySortOptionRepositoryImpl(dataStore = get())
    }

    factory<CategoryRepository> {
        CategoryRepositoryImpl(categoryDao = get(), categoryDBConverter = get())
    }
}
