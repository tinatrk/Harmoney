package com.example.harmoney.core.di

import com.example.harmoney.presentation.category.viewModel.CategoryViewModel
import com.example.harmoney.presentation.categoryList.viewModel.CategoryListViewModel
import com.example.harmoney.presentation.categoryStatistics.viewModel.CategoryStatisticsViewModel
import com.example.harmoney.presentation.transaction.viewModel.TransactionViewModel
import com.example.harmoney.presentation.transactionList.viewModel.TransactionListViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        CategoryStatisticsViewModel()
    }

    viewModel { (categoryId: Long?) ->
        TransactionListViewModel(categoryId = categoryId)
    }

    viewModel { (categoryId: Long?, transactionId: Long?) ->
        TransactionViewModel(categoryId = categoryId, transactionId = transactionId)
    }

    viewModel { (categoryTypeId: Long?) ->
        CategoryListViewModel(categoryTypeId = categoryTypeId)
    }

    viewModel { (categoryId: Long?, categoryTypeId: Long?) ->
        CategoryViewModel(categoryId = categoryId, categoryTypeId = categoryTypeId)
    }
}
