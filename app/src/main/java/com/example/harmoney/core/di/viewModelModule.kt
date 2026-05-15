package com.example.harmoney.core.di

import androidx.lifecycle.SavedStateHandle
import com.example.harmoney.base.ResourceProvider
import com.example.harmoney.presentation.calculator.viewModel.CalculatorHandler
import com.example.harmoney.presentation.calculator.viewModel.CalculatorViewModel
import com.example.harmoney.presentation.category.viewModel.CategoryViewModel
import com.example.harmoney.presentation.categoryList.viewModel.CategoryListViewModel
import com.example.harmoney.presentation.categoryStatistics.viewModel.CategoryStatisticsViewModel
import com.example.harmoney.presentation.test.TestDataSource
import com.example.harmoney.presentation.converters.CategoryStatisticsUiConverter
import com.example.harmoney.presentation.converters.CategoryStatisticsUiConverterImpl
import com.example.harmoney.presentation.converters.CategoryUiConverter
import com.example.harmoney.presentation.converters.CategoryUiConverterImpl
import com.example.harmoney.presentation.converters.NumbersFormatter
import com.example.harmoney.presentation.converters.NumbersFormatterImpl
import com.example.harmoney.presentation.transaction.viewModel.TransactionViewModel
import com.example.harmoney.presentation.transactionList.viewModel.TransactionListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        CategoryStatisticsViewModel(
            test = get(),
            categoryConverter = get(),
            numbersFormatter = get(),
            resourceProvider = get()
        )
    }

    viewModel { (categoryId: Long?) ->
        TransactionListViewModel(categoryId = categoryId)
    }

    viewModel { (categoryId: Long?, transactionId: Long?) ->
        TransactionViewModel(
            categoryId = categoryId,
            transactionId = transactionId,
            savedStateHandle = get()
        )
    }

    viewModel { (categoryTypeId: Long?) ->
        CategoryListViewModel(categoryTypeId = categoryTypeId, savedStateHandle = get())
    }

    viewModel { (categoryId: Long?, categoryTypeId: Long?) ->
        CategoryViewModel(categoryId = categoryId, categoryTypeId = categoryTypeId)
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

    single {
        ResourceProvider(androidContext())
    }

    //временный класс
    single {
        TestDataSource()
    }
}
