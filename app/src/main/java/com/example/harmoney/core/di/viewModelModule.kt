package com.example.harmoney.core.di

import androidx.lifecycle.SavedStateHandle
import com.example.harmoney.MainViewModel
import com.example.harmoney.core.util.AndroidStringProvider
import com.example.harmoney.core.util.StringProvider
import com.example.harmoney.presentation.calculator.viewModel.CalculatorHandler
import com.example.harmoney.presentation.calculator.viewModel.CalculatorViewModel
import com.example.harmoney.presentation.category.viewModel.CategoryViewModel
import com.example.harmoney.presentation.categoryList.viewModel.CategoryListViewModel
import com.example.harmoney.presentation.categoryStatistics.converters.CategoryStatisticsContentConverter
import com.example.harmoney.presentation.categoryStatistics.converters.CategoryStatisticsContentConverterImpl
import com.example.harmoney.presentation.categoryStatistics.converters.CategoryStatisticsUiConverter
import com.example.harmoney.presentation.categoryStatistics.converters.CategoryStatisticsUiConverterImpl
import com.example.harmoney.presentation.categoryStatistics.flows.CategoryStatisticsAccountFlowsFactory
import com.example.harmoney.presentation.categoryStatistics.flows.CategoryStatisticsSettingsFlows
import com.example.harmoney.presentation.categoryStatistics.flows.CategoryStatisticsStatisticsFlowsFactory
import com.example.harmoney.presentation.categoryStatistics.viewModel.CategoryStatisticsViewModel
import com.example.harmoney.presentation.converters.AccountContentConverter
import com.example.harmoney.presentation.converters.AccountContentConverterImpl
import com.example.harmoney.presentation.converters.AccountScopeUiConverter
import com.example.harmoney.presentation.converters.AccountScopeUiConverterImpl
import com.example.harmoney.presentation.converters.CategoryUiConverter
import com.example.harmoney.presentation.converters.CategoryUiConverterImpl
import com.example.harmoney.presentation.converters.ConvertedTotalFormatter
import com.example.harmoney.presentation.converters.ConvertedTotalFormatterImpl
import com.example.harmoney.presentation.converters.CurrencyUiConverter
import com.example.harmoney.presentation.converters.CurrencyUiConverterImpl
import com.example.harmoney.presentation.converters.DateFormatter
import com.example.harmoney.presentation.converters.DateFormatterImpl
import com.example.harmoney.presentation.converters.ExchangeRateFormatter
import com.example.harmoney.presentation.converters.ExchangeRateFormatterImpl
import com.example.harmoney.presentation.converters.NumbersFormatter
import com.example.harmoney.presentation.converters.NumbersFormatterImpl
import com.example.harmoney.presentation.converters.OneDayTransactionsUiConverter
import com.example.harmoney.presentation.converters.OneDayTransactionsUiConverterImpl
import com.example.harmoney.presentation.converters.StatisticsPeriodUiConverter
import com.example.harmoney.presentation.converters.StatisticsPeriodUiConverterImpl
import com.example.harmoney.presentation.converters.TransactionFilterUiConverter
import com.example.harmoney.presentation.converters.TransactionFilterUiConverterImpl
import com.example.harmoney.presentation.converters.TransactionUiConverter
import com.example.harmoney.presentation.converters.TransactionUiConverterImpl
import com.example.harmoney.presentation.currency.viewModel.CurrencyViewModel
import com.example.harmoney.presentation.test.TestDataSource
import com.example.harmoney.presentation.transaction.viewModel.TransactionViewModel
import com.example.harmoney.presentation.transactionList.viewModel.TransactionListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    // region viewModel
    viewModel {
        CategoryStatisticsViewModel(
            savedStateHandle = get(),
            setThemeUseCase = get(),

            categoryStatisticsSettingsFlows = get(),
            categoryStatisticsAccountFlowsFactory = get(),
            categoryStatisticsStatisticsFlowsFactory = get(),

            statisticsPeriodUiConverter = get(),
            categoryStatisticsContentConverter = get(),
            accountContentConverter = get(),
        )
    }

    viewModel { (categoryId: Long?) ->
        TransactionListViewModel(
            categoryId = categoryId,
            test = get(),
            oneDayTransactionsUiConverter = get(),
            numberFormatter = get(),
            transactionFilterUiConverter = get(),
            statisticsPeriodUiConverter = get(),
            getStatisticsPeriodsUseCase = get(),
            sessionStateHolder = get()
        )
    }

    viewModel { (categoryId: Long?, transactionId: Long?) ->
        TransactionViewModel(
            sessionSateHolder = get(),
            categoryId = categoryId,
            transactionId = transactionId,
            test = get(),
            numbersFormatter = get(),
            dateFormatter = get(),
            transactionUiConverter = get(),
            categoryUiConverter = get()
        )
    }

    viewModel {
        CategoryListViewModel(
            sessionSateHolder = get(),
            categoryUiConverter = get(),
            categorySortOptionInteractor = get(),
            getCategoryListUseCase = get(),
            updateCategoryUserOrderUseCase = get()
        )
    }

    viewModel { (categoryId: Long?) ->
        CategoryViewModel(
            sessionStateHolder = get(),
            categoryId = categoryId,
            getCategoryUseCase = get(),
            addCategoryUseCase = get(),
            updateCategoryUseCase = get(),
            deleteCategoryUseCase = get(),
            checkCategoryAlreadyExistsUseCase = get()
        )
    }

    viewModel {
        CalculatorViewModel(calculatorHandler = get())
    }

    viewModel { (currencyId: Long) ->
        CurrencyViewModel(
            selectedCurrencyId = currencyId,
            savedStateHandle = get(),
            currencyUiConverter = get(),
            getSupportedCurrencyListUseCase = get()
        )
    }

    viewModel {
        MainViewModel(getIsThemeDarkUseCase = get())
    }
    // endregion


    // region ui converters
    factory<CategoryStatisticsUiConverter> {
        CategoryStatisticsUiConverterImpl(
            categoryUiConverter = get(),
            numberFormatter = get(),
            convertedTotalFormatter = get(),
        )
    }

    factory<CategoryUiConverter> {
        CategoryUiConverterImpl()
    }

    factory<NumbersFormatter> {
        NumbersFormatterImpl()
    }

    factory<DateFormatter> {
        DateFormatterImpl()
    }

    factory<OneDayTransactionsUiConverter> {
        OneDayTransactionsUiConverterImpl(
            transactionUiConverter = get(),
            numberFormatter = get(),
            dateFormatter = get()
        )
    }

    factory<TransactionUiConverter> {
        TransactionUiConverterImpl(
            categoryUiConverter = get(),
            numbersFormatter = get(),
            dateFormatter = get()
        )
    }

    factory<TransactionFilterUiConverter> {
        TransactionFilterUiConverterImpl()
    }

    factory<StatisticsPeriodUiConverter> {
        StatisticsPeriodUiConverterImpl(dateFormatter = get())
    }

    factory<CurrencyUiConverter> {
        CurrencyUiConverterImpl(stringProvider = get())
    }

    factory<AccountScopeUiConverter> {
        AccountScopeUiConverterImpl(
            stringProvider = get(),
            numbersFormatter = get(),
            convertedTotalFormatter = get()
        )
    }

    factory<ExchangeRateFormatter> {
        ExchangeRateFormatterImpl(
            stringProvider = get(),
            dateFormatter = get(),
            numbersFormatter = get()
        )
    }

    factory<ConvertedTotalFormatter> {
        ConvertedTotalFormatterImpl(numbersFormatter = get(), stringProvider = get())
    }

    factory<CategoryStatisticsContentConverter> {
        CategoryStatisticsContentConverterImpl(
            numbersFormatter = get(),
            moneyCalculator = get(),
            categoryStatisticsUiConverter = get(),
            convertedTotalFormatter = get()
        )
    }

    factory<AccountContentConverter> {
        AccountContentConverterImpl(
            accountScopeUiConverter = get(), exchangeRateFormatter = get()
        )
    }
    // endregion


    // region others
    single {
        CalculatorHandler()
    }

    //временный класс
    single {
        TestDataSource()
    }

    factory<StringProvider> {
        AndroidStringProvider(context = androidContext())
    }

    single {
        SavedStateHandle()
    }

    factory {
        CategoryStatisticsSettingsFlows(
            sessionStateHolder = get(),
            getStatisticsPeriodsUseCase = get(),
            reportingCurrencyInteractor = get(),
            firstDayMonthInteractor = get()
        )
    }

    factory {
        CategoryStatisticsAccountFlowsFactory(
            selectedAccountScopeInteractor = get(),
            getAccountListUseCase = get(),
            getAccountScopesUseCase = get(),
            getAccountScopesWithBalanceInteractor = get()
        )
    }

    factory {
        CategoryStatisticsStatisticsFlowsFactory(
            getCategoryStatisticsUseCase = get(),
            getCategoriesTotalUseCase = get(),
            prepareCategoryStatisticsInteractor = get()
        )
    }
    // endregion
}
