package com.example.harmoney.core.di

import com.example.harmoney.domain.settings.period.api.useCase.FirstDayMonthInteractor
import com.example.harmoney.domain.settings.period.api.useCase.GetStatisticsPeriodsUseCase
import com.example.harmoney.domain.settings.period.impl.FirstDayMonthInteractorImpl
import com.example.harmoney.domain.settings.period.impl.GetStatisticsPeriodsUseCaseImpl
import com.example.harmoney.domain.settings.period.impl.StatisticsPeriodCalculator
import com.example.harmoney.domain.settings.theme.api.useCase.GetIsThemeDarkUseCase
import com.example.harmoney.domain.settings.theme.api.useCase.SetThemeUseCase
import com.example.harmoney.domain.settings.theme.impl.GetIsThemeDarkUseCaseImpl
import com.example.harmoney.domain.settings.theme.impl.SetThemeUseCaseImpl
import org.koin.dsl.module

val useCaseModule = module {
    factory<GetIsThemeDarkUseCase> {
        GetIsThemeDarkUseCaseImpl(themeRepository = get())
    }

    factory<SetThemeUseCase> {
        SetThemeUseCaseImpl(themeRepository = get())
    }

    factory<GetStatisticsPeriodsUseCase> {
        GetStatisticsPeriodsUseCaseImpl(repository = get(), calculator = get())
    }

    factory {
        StatisticsPeriodCalculator()
    }

    factory<FirstDayMonthInteractor> {
        FirstDayMonthInteractorImpl(repository = get())
    }
}
