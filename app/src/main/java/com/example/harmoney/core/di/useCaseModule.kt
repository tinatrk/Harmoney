package com.example.harmoney.core.di

import com.example.harmoney.domain.account.api.useCase.AddAccountUseCase
import com.example.harmoney.domain.account.api.useCase.DeleteAccountUseCase
import com.example.harmoney.domain.account.api.useCase.GetAccountListUseCase
import com.example.harmoney.domain.account.api.useCase.GetAccountScopesUseCase
import com.example.harmoney.domain.account.api.useCase.GetAccountScopesWithBalanceInteractor
import com.example.harmoney.domain.account.api.useCase.GetAccountUseCase
import com.example.harmoney.domain.account.api.useCase.SelectedAccountScopeInteractor
import com.example.harmoney.domain.account.api.useCase.UpdateAccountUseCase
import com.example.harmoney.domain.account.api.useCase.UpdateAccountUserOrderUseCase
import com.example.harmoney.domain.account.impl.AddAccountUseCaseImpl
import com.example.harmoney.domain.account.impl.DeleteAccountUseCaseImpl
import com.example.harmoney.domain.account.impl.GetAccountListUseCaseImpl
import com.example.harmoney.domain.account.impl.GetAccountScopesUseCaseImpl
import com.example.harmoney.domain.account.impl.GetAccountScopesWithBalanceInteractorImpl
import com.example.harmoney.domain.account.impl.GetAccountUseCaseImpl
import com.example.harmoney.domain.account.impl.SelectedAccountScopeInteractorImpl
import com.example.harmoney.domain.account.impl.UpdateAccountUseCaseImpl
import com.example.harmoney.domain.account.impl.UpdateAccountUserOrderUseCaseImpl
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
import com.example.harmoney.domain.converter.ExchangeRateResultConverter
import com.example.harmoney.domain.converter.ExchangeRateResultConverterImpl
import com.example.harmoney.domain.currencyList.api.useCase.GetSupportedCurrencyListUseCase
import com.example.harmoney.domain.currencyList.impl.GetSupportedCurrencyListUseCaseImpl
import com.example.harmoney.domain.exchangeRate.api.useCase.ConvertCategoryStatisticsUseCase
import com.example.harmoney.domain.exchangeRate.api.useCase.ConvertTotalMoneyUseCase
import com.example.harmoney.domain.exchangeRate.api.useCase.CalculateConvertedTotalUseCase
import com.example.harmoney.domain.exchangeRate.api.useCase.GetExchangeRatesUseCase
import com.example.harmoney.domain.exchangeRate.impl.ConvertCategoryStatisticsUseCaseImpl
import com.example.harmoney.domain.exchangeRate.impl.ConvertTotalMoneyUseCaseImpl
import com.example.harmoney.domain.exchangeRate.impl.CalculateConvertedTotalUseCaseImpl
import com.example.harmoney.domain.exchangeRate.impl.GetExchangeRatesUseCaseImpl
import com.example.harmoney.domain.settings.categorySortingMode.api.useCase.CategorySortOptionInteractor
import com.example.harmoney.domain.settings.categorySortingMode.impl.CategorySortOptionInteractorImpl
import com.example.harmoney.domain.settings.currency.api.useCase.ReportingCurrencyInteractor
import com.example.harmoney.domain.settings.currency.impl.ReportingCurrencyInteractorImpl
import com.example.harmoney.domain.settings.period.api.useCase.FirstDayMonthInteractor
import com.example.harmoney.domain.settings.period.api.useCase.GetStatisticsPeriodsUseCase
import com.example.harmoney.domain.settings.period.impl.FirstDayMonthInteractorImpl
import com.example.harmoney.domain.settings.period.impl.GetStatisticsPeriodsUseCaseImpl
import com.example.harmoney.domain.settings.period.impl.StatisticsPeriodCalculator
import com.example.harmoney.domain.settings.theme.api.useCase.GetIsThemeDarkUseCase
import com.example.harmoney.domain.settings.theme.api.useCase.SetThemeUseCase
import com.example.harmoney.domain.settings.theme.impl.GetIsThemeDarkUseCaseImpl
import com.example.harmoney.domain.settings.theme.impl.SetThemeUseCaseImpl
import com.example.harmoney.domain.statistics.api.useCase.GetCategoriesSummaryUseCase
import com.example.harmoney.domain.statistics.api.useCase.GetCategoriesTotalUseCase
import com.example.harmoney.domain.statistics.api.useCase.GetCategoryStatisticsUseCase
import com.example.harmoney.domain.statistics.api.useCase.PrepareCategoryStatisticsInteractor
import com.example.harmoney.domain.statistics.converter.MoneyCalculator
import com.example.harmoney.domain.statistics.converter.MoneyCalculatorImpl
import com.example.harmoney.domain.statistics.impl.GetCategoriesSummaryUseCaseImpl
import com.example.harmoney.domain.statistics.impl.GetCategoriesTotalUseCaseImpl
import com.example.harmoney.domain.statistics.impl.GetCategoryStatisticsUseCaseImpl
import com.example.harmoney.domain.statistics.impl.PrepareCategoryStatisticsInteractorImpl
import com.example.harmoney.domain.transaction.api.useCase.AddTransactionUseCase
import com.example.harmoney.domain.transaction.api.useCase.DeleteTransactionUseCase
import com.example.harmoney.domain.transaction.api.useCase.GetTransactionUseCase
import com.example.harmoney.domain.transaction.api.useCase.GetTransactionsSummaryUseCase
import com.example.harmoney.domain.transaction.api.useCase.UpdateTransactionUseCase
import com.example.harmoney.domain.transaction.impl.AddTransactionUseCaseImpl
import com.example.harmoney.domain.transaction.impl.DeleteTransactionUseCaseImpl
import com.example.harmoney.domain.transaction.impl.GetTransactionUseCaseImpl
import com.example.harmoney.domain.transaction.impl.GetTransactionsSummaryUseCaseImpl
import com.example.harmoney.domain.transaction.impl.UpdateTransactionUseCaseImpl
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
        GetCategoryListUseCaseImpl(categoryRepository = get())
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


    // region transaction
    factory<AddTransactionUseCase> {
        AddTransactionUseCaseImpl(repository = get())
    }

    factory<DeleteTransactionUseCase> {
        DeleteTransactionUseCaseImpl(repository = get())
    }

    factory<UpdateTransactionUseCase> {
        UpdateTransactionUseCaseImpl(repository = get())
    }

    factory<GetTransactionUseCase> {
        GetTransactionUseCaseImpl(repository = get())
    }

    factory<GetTransactionsSummaryUseCase> {
        GetTransactionsSummaryUseCaseImpl(repository = get())
    }

    factory<GetCategoriesSummaryUseCase> {
        GetCategoriesSummaryUseCaseImpl(repository = get())
    }
    // endregion


    // region reporting currency
    factory<ReportingCurrencyInteractor> {
        ReportingCurrencyInteractorImpl(repository = get())
    }
    // endregion


    // region currency list
    factory<GetSupportedCurrencyListUseCase> {
        GetSupportedCurrencyListUseCaseImpl(repository = get())
    }
    // endregion

    // region statistics
    factory<GetCategoryStatisticsUseCase> {
        GetCategoryStatisticsUseCaseImpl(repository = get())
    }

    factory<GetCategoriesTotalUseCase> {
        GetCategoriesTotalUseCaseImpl(repository = get())
    }

    factory<PrepareCategoryStatisticsInteractor> {
        PrepareCategoryStatisticsInteractorImpl(
            moneyCalculator = get(),
            convertCategoryStatisticsUseCase = get(),
            convertTotalMoneyUseCase = get()
        )
    }

    factory<MoneyCalculator> {
        MoneyCalculatorImpl()
    }
    // endregion

    // region account
    factory<AddAccountUseCase> {
        AddAccountUseCaseImpl(repository = get())
    }

    factory<UpdateAccountUseCase> {
        UpdateAccountUseCaseImpl(repository = get())
    }

    factory<DeleteAccountUseCase> {
        DeleteAccountUseCaseImpl(repository = get())
    }

    factory<GetAccountUseCase> {
        GetAccountUseCaseImpl(repository = get())
    }

    factory<GetAccountListUseCase> {
        GetAccountListUseCaseImpl(repository = get())
    }

    factory<UpdateAccountUserOrderUseCase> {
        UpdateAccountUserOrderUseCaseImpl(repository = get())
    }

    factory<CheckCategoryAlreadyExistsUseCase> {
        CheckCategoryAlreadyExistsUseCaseImpl(repository = get())
    }

    factory<SelectedAccountScopeInteractor> {
        SelectedAccountScopeInteractorImpl(repository = get())
    }

    factory<GetAccountScopesUseCase> {
        GetAccountScopesUseCaseImpl()
    }

    factory<GetAccountScopesWithBalanceInteractor> {
        GetAccountScopesWithBalanceInteractorImpl(convertTotalMoneyUseCase = get())
    }
    // endregion

    // region exchangedRate
    factory<ConvertCategoryStatisticsUseCase> {
        ConvertCategoryStatisticsUseCaseImpl(
            getExchangeRatesUseCase = get(),
            calculateConvertedTotalUseCase = get(),
            exchangeRateResultConverter = get(),
        )
    }

    factory<ConvertTotalMoneyUseCase> {
        ConvertTotalMoneyUseCaseImpl(
            getExchangeRatesUseCase = get(),
            calculateConvertedTotalUseCase = get(),
            exchangeRateResultConverter = get(),
        )
    }

    factory<GetExchangeRatesUseCase> {
        GetExchangeRatesUseCaseImpl(repository = get())
    }

    factory<CalculateConvertedTotalUseCase> {
        CalculateConvertedTotalUseCaseImpl()
    }

    factory<ExchangeRateResultConverter> {
        ExchangeRateResultConverterImpl()
    }
    // endregion
}
