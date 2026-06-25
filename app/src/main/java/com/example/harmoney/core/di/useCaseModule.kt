package com.example.harmoney.core.di

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
}
