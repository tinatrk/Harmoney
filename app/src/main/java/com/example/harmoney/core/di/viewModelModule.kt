package com.example.harmoney.core.di

import androidx.lifecycle.SavedStateHandle
import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.domain.models.StatisticsPeriod
import com.example.harmoney.presentation.calculator.viewModel.CalculatorHandler
import com.example.harmoney.presentation.calculator.viewModel.CalculatorViewModel
import com.example.harmoney.presentation.category.viewModel.CategoryViewModel
import com.example.harmoney.presentation.categoryList.viewModel.CategoryListViewModel
import com.example.harmoney.presentation.categoryStatistics.viewModel.CategoryStatisticsViewModel
import com.example.harmoney.presentation.converters.CategoryStatisticsUiConverter
import com.example.harmoney.presentation.converters.CategoryStatisticsUiConverterImpl
import com.example.harmoney.presentation.converters.CategoryUiConverter
import com.example.harmoney.presentation.converters.CategoryUiConverterImpl
import com.example.harmoney.presentation.converters.NumbersFormatter
import com.example.harmoney.presentation.converters.NumbersFormatterImpl
import com.example.harmoney.presentation.converters.OneDayTransactionsUiConverter
import com.example.harmoney.presentation.converters.OneDayTransactionsUiConverterImpl
import com.example.harmoney.presentation.converters.TransactionUiConverter
import com.example.harmoney.presentation.converters.TransactionUiConverterImpl
import com.example.harmoney.presentation.converters.TransactionsFilterUiConverter
import com.example.harmoney.presentation.converters.TransactionsFilterUiConverterImpl
import com.example.harmoney.presentation.sharedViewModel.SharedCategoryTypeViewModel
import com.example.harmoney.presentation.sharedViewModel.SharedStatisticsPeriodViewModel
import com.example.harmoney.presentation.test.TestDataSource
import com.example.harmoney.presentation.transaction.viewModel.TransactionViewModel
import com.example.harmoney.presentation.transactionList.viewModel.TransactionListViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { (categoryType: CategoryType, statisticsPeriod: StatisticsPeriod) ->
        CategoryStatisticsViewModel(
            categoryType = categoryType,
            statisticsPeriod = statisticsPeriod,
            test = get(),
            categoryStatisticsUiConverter = get(),
            numbersFormatter = get(),
        )
    }

    viewModel { (
                    categoryType: CategoryType,
                    statisticsPeriod: StatisticsPeriod,
                    categoryId: Long?) ->
        TransactionListViewModel(
            categoryType = categoryType,
            statisticsPeriod = statisticsPeriod,
            categoryId = categoryId,
            test = get(),
            oneDayTransactionsUiConverter = get(),
            numberFormatter = get(),
            transactionsFilterUiConverter = get()
        )
    }

    viewModel { (categoryType: CategoryType, categoryId: Long?, transactionId: Long?) ->
        TransactionViewModel(
            categoryType = categoryType,
            categoryId = categoryId,
            transactionId = transactionId,
            savedStateHandle = get()
        )
    }

    viewModel { (categoryType: CategoryType, isCategorySelectionMode: Boolean) ->
        CategoryListViewModel(
            categoryType = categoryType,
            isCategorySelectionMode = isCategorySelectionMode,
            savedStateHandle = get(),
        )
    }

    viewModel { (categoryType: CategoryType, categoryId: Long?) ->
        CategoryViewModel(categoryType = categoryType, categoryId = categoryId)
    }

    viewModel {
        SharedCategoryTypeViewModel()
    }

    viewModel {
        SharedStatisticsPeriodViewModel()
    }

    single {
        SavedStateHandle()
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

    factory<OneDayTransactionsUiConverter> {
        OneDayTransactionsUiConverterImpl(transactionUiConverter = get(), numberFormatter = get())
    }

    factory<TransactionUiConverter> {
        TransactionUiConverterImpl(categoryUiConverter = get(), numbersFormatter = get())
    }

    factory<TransactionsFilterUiConverter> {
        TransactionsFilterUiConverterImpl()
    }

    //временный класс
    single {
        TestDataSource()
    }
}
