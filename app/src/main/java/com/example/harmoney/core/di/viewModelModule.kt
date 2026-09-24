package com.example.harmoney.core.di

import androidx.lifecycle.SavedStateHandle
import com.example.harmoney.MainViewModel
import com.example.harmoney.core.util.AndroidStringProvider
import com.example.harmoney.core.util.StringProvider
import com.example.harmoney.presentation.calculator.viewModel.CalculatorHandler
import com.example.harmoney.presentation.calculator.viewModel.CalculatorViewModel
import com.example.harmoney.presentation.category.viewModel.CategoryViewModel
import com.example.harmoney.presentation.categoryList.viewModel.CategoryListViewModel
import com.example.harmoney.presentation.categoryStatistics.viewModel.CategoryStatisticsViewModel
import com.example.harmoney.presentation.converters.CategoryStatisticsUiConverter
import com.example.harmoney.presentation.converters.CategoryStatisticsUiConverterImpl
import com.example.harmoney.presentation.converters.CategoryUiConverter
import com.example.harmoney.presentation.converters.CategoryUiConverterImpl
import com.example.harmoney.presentation.converters.CurrencyUiConverter
import com.example.harmoney.presentation.converters.CurrencyUiConverterImpl
import com.example.harmoney.presentation.converters.DateFormatter
import com.example.harmoney.presentation.converters.DateFormatterImpl
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
            test = get(),
            categoryStatisticsUiConverter = get(),
            numbersFormatter = get(),
            setThemeUseCase = get(),
            firstDayMonthInteractor = get(),
            getStatisticsPeriodsUseCase = get(),
            statisticsPeriodUiConverter = get(),
            sessionSateHolder = get(),
            getReportingCurrencyUseCase = get(),
            setReportingCurrencyUseCase = get(),
            savedStateHandle = get(),
            getCategoryStatisticsUseCase = get()
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
        CategoryStatisticsUiConverterImpl(categoryUiConverter = get(), numberFormatter = get())
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
    // endregion
}
