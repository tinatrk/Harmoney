package com.example.harmoney.presentation.transaction.models

import androidx.compose.runtime.Stable
import com.example.harmoney.domain.models.CategoryType

@Stable
data class TransactionState(
    val categoryId: Long? = null,
    val transactionId: Long? = null,
    val isCreateTransactionScreen: Boolean = true,
    val categoryType: CategoryType = CategoryType.Expenses,
    val selectedTabIndex: Int = CategoryType.Expenses.ordinal,
    // то, что зависит от выбора таб вкладки
    val categoryInfo: String = ""
)
