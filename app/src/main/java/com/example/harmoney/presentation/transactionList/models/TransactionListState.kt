package com.example.harmoney.presentation.transactionList.models

import com.example.harmoney.domain.models.CategoryType

data class TransactionListState(
    val currentBalance: Double = 0.0,
    val categoryId: Long? = null,
    val categoryType: CategoryType = CategoryType.Expenses,
    val selectedTabIndex: Int = CategoryType.Expenses.ordinal,
    // то, что зависит от выбора таб вкладки
    val categoryInfo: String = ""
)
