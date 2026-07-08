package com.example.harmoney.core.di

import com.example.harmoney.MainViewModel
import com.example.harmoney.presentation.calculator.viewModel.CalculatorHandler
import com.example.harmoney.presentation.calculator.viewModel.CalculatorViewModel
import com.example.harmoney.presentation.category.viewModel.CategoryViewModel
import com.example.harmoney.presentation.categoryList.viewModel.CategoryListViewModel
import com.example.harmoney.presentation.categoryStatistics.viewModel.CategoryStatisticsViewModel
import com.example.harmoney.presentation.converters.CategoryStatisticsUiConverter
import com.example.harmoney.presentation.converters.CategoryStatisticsUiConverterImpl
import com.example.harmoney.presentation.converters.CategoryUiConverter
import com.example.harmoney.presentation.converters.CategoryUiConverterImpl
import com.example.harmoney.presentation.converters.DateFormatter
import com.example.harmoney.presentation.converters.DateFormatterImpl
import com.example.harmoney.presentation.converters.NumbersFormatter
import com.example.harmoney.presentation.converters.NumbersFormatterImpl
import com.example.harmoney.presentation.converters.OneDayTransactionsUiConverter
import com.example.harmoney.presentation.converters.OneDayTransactionsUiConverterImpl
import com.example.harmoney.presentation.converters.StatisticsPeriodUiConverter
import com.example.harmoney.presentation.converters.StatisticsPeriodUiConverterImpl
import com.example.harmoney.presentation.converters.TransactionUiConverter
import com.example.harmoney.presentation.converters.TransactionUiConverterImpl
import com.example.harmoney.presentation.converters.TransactionsFilterUiConverter
import com.example.harmoney.presentation.converters.TransactionsFilterUiConverterImpl
import com.example.harmoney.presentation.test.TestDataSource
import com.example.harmoney.presentation.transaction.viewModel.TransactionViewModel
import com.example.harmoney.presentation.transactionList.viewModel.TransactionListViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        CategoryStatisticsViewModel(
            test = get(),
            categoryStatisticsUiConverter = get(),
            numbersFormatter = get(),
            setThemeUseCase = get(),
            firstDayMonthInteractor = get(),
            getStatisticsPeriodsUseCase = get(),
            statisticsPeriodUiConverter = get(),
            sessionSateHolder = get()
        )
    }

    viewModel { (categoryId: Long?) ->
        TransactionListViewModel(
            categoryId = categoryId,
            test = get(),
            oneDayTransactionsUiConverter = get(),
            numberFormatter = get(),
            transactionsFilterUiConverter = get(),
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
            test = get(),
            categoryUiConverter = get()
        )
    }

    viewModel { (categoryId: Long?) ->
        CategoryViewModel(
            sessionSateHolder = get(),
            categoryId = categoryId,
            test = get(),
            categoryUiConverter = get()
        )
    }

    viewModel {
        CalculatorViewModel(calculatorHandler = get())
    }

    single {
        CalculatorHandler()
    }

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

    factory<TransactionsFilterUiConverter> {
        TransactionsFilterUiConverterImpl()
    }

    factory<StatisticsPeriodUiConverter> {
        StatisticsPeriodUiConverterImpl(dateFormatter = get())
    }

    //временный класс
    single {
        TestDataSource()
    }

    viewModel {
        MainViewModel(getIsThemeDarkUseCase = get())
    }
}
