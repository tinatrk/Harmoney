package com.example.harmoney.domain.transaction.api.repository

import com.example.harmoney.core.util.Resource
import com.example.harmoney.domain.models.CategoriesSummary
import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.domain.models.StatisticsPeriod
import com.example.harmoney.domain.models.Transaction
import com.example.harmoney.domain.models.TransactionFilter
import com.example.harmoney.domain.models.TransactionsSummary
import com.example.harmoney.domain.transaction.models.TransactionFailure
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    suspend fun addTransaction(transaction: Transaction): Resource<Unit, TransactionFailure>

    suspend fun updateTransaction(transaction: Transaction): Resource<Unit, TransactionFailure>

    suspend fun deleteTransaction(transaction: Transaction): Resource<Unit, TransactionFailure>

    suspend fun getTransaction(transactionId: Long): Resource<Transaction, TransactionFailure>

    fun getTransactionsSummary(
        categoryType: CategoryType,
        period: StatisticsPeriod,
        filter: TransactionFilter
    )
            : Flow<Resource<TransactionsSummary, TransactionFailure>>

    fun getFilterList(categoryType: CategoryType)
            : Flow<Resource<List<TransactionFilter>, TransactionFailure>>

    // в этом репозитории, т.к. происходит группировка транзакций по категориям
    fun getCategoriesSummary(
        categoryType: CategoryType,
        period: StatisticsPeriod
    ): Flow<Resource<CategoriesSummary, TransactionFailure>>
}
