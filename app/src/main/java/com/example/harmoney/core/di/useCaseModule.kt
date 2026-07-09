package com.example.harmoney.core.di

import com.example.harmoney.domain.category.api.useCase.AddCategoryUseCase
import com.example.harmoney.domain.category.api.useCase.CheckCategoryAlreadyExistsUseCase
import com.example.harmoney.domain.category.api.useCase.DeleteCategoryUseCase
import com.example.harmoney.domain.category.api.useCase.GetCategoryListUseCase
import com.example.harmoney.domain.category.api.useCase.GetCategoryUseCase
import com.example.harmoney.domain.category.api.useCase.UpdateCategoryUseCase
import com.example.harmoney.domain.category.api.useCase.UpdateCategoryUserOrderUseCase
import com.example.harmoney.domain.category.impl.AddCategoryUseCaseImpl
import com.example.harmoney.domain.category.impl.CheckCategoryAlreadyExistsUseCaseImpl
import com.example.harmoney.domain.category.impl.DeleteCategoryUseCaseImpl
import com.example.harmoney.domain.category.impl.GetCategoryListUseCaseImpl
import com.example.harmoney.domain.category.impl.GetCategoryUseCaseImpl
import com.example.harmoney.domain.category.impl.UpdateCategoryUseCaseImpl
import com.example.harmoney.domain.category.impl.UpdateCategoryUserOrderUseCaseImpl
import com.example.harmoney.domain.settings.categorySortingMode.api.useCase.CategorySortOptionInteractor
import com.example.harmoney.domain.settings.categorySortingMode.impl.CategorySortOptionInteractorImpl
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

    // region theme
    factory<GetIsThemeDarkUseCase> {
        GetIsThemeDarkUseCaseImpl(themeRepository = get())
    }

    factory<SetThemeUseCase> {
        SetThemeUseCaseImpl(themeRepository = get())
    }
    // endregion

    // region statistics periods
    factory<GetStatisticsPeriodsUseCase> {
        GetStatisticsPeriodsUseCaseImpl(repository = get(), calculator = get())
    }

    factory {
        StatisticsPeriodCalculator()
    }
    // endregion

    // region first day month
    factory<FirstDayMonthInteractor> {
        FirstDayMonthInteractorImpl(repository = get())
    }
    // endregion

    // region category
    factory<CategorySortOptionInteractor> {
        CategorySortOptionInteractorImpl(repository = get())
    }

    factory<AddCategoryUseCase> {
        AddCategoryUseCaseImpl(repository = get())
    }

    factory<CheckCategoryAlreadyExistsUseCase> {
        CheckCategoryAlreadyExistsUseCaseImpl(repository = get())
    }

    factory<DeleteCategoryUseCase> {
        DeleteCategoryUseCaseImpl(repository = get())
    }

    factory<GetCategoryListUseCase> {
        GetCategoryListUseCaseImpl(categoryRepository = get(), sortOptionRepository = get())
    }

    factory<GetCategoryUseCase> {
        GetCategoryUseCaseImpl(repository = get())
    }

    factory<UpdateCategoryUseCase> {
        UpdateCategoryUseCaseImpl(repository = get())
    }

    factory<UpdateCategoryUserOrderUseCase> {
        UpdateCategoryUserOrderUseCaseImpl(repository = get())
    }
    // endregion
}
