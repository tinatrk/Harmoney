package com.example.harmoney.data.transaction.impl

import com.example.harmoney.core.util.Resource
import com.example.harmoney.domain.models.CategoriesSummary
import com.example.harmoney.domain.models.Category
import com.example.harmoney.domain.models.CategoryColors
import com.example.harmoney.domain.models.CategoryIcon
import com.example.harmoney.domain.models.CategoryIcons
import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.domain.models.StatisticsPeriod
import com.example.harmoney.domain.models.Transaction
import com.example.harmoney.domain.models.TransactionFilter
import com.example.harmoney.domain.models.TransactionsSummary
import com.example.harmoney.domain.transaction.api.repository.TransactionRepository
import com.example.harmoney.domain.transaction.models.TransactionFailure
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDate

class TransactionRepositoryImpl : TransactionRepository {
    override suspend fun addTransaction(transaction: Transaction)
            : Resource<Unit, TransactionFailure> {
        return Resource.Success(Unit)
    }

    override suspend fun deleteTransaction(transaction: Transaction)
            : Resource<Unit, TransactionFailure> {
        return Resource.Success(Unit)
    }

    override suspend fun updateTransaction(transaction: Transaction)
            : Resource<Unit, TransactionFailure> {
        return Resource.Success(Unit)
    }

    override fun getTransactionsSummary(
        categoryType: CategoryType,
        period: StatisticsPeriod,
        filter: TransactionFilter
    ): Flow<Resource<TransactionsSummary, TransactionFailure>> =
        flow {
            emit(
                Resource.Success(
                    TransactionsSummary(
                        days = listOf(),
                        totalAmount = 0.0
                    )
                )
            )
        }

    override suspend fun getTransaction(transactionId: Long)
            : Resource<Transaction, TransactionFailure> {
        return Resource.Success(
            Transaction(
                id = 0, category = Category(
                    id = 0, name = "", type = CategoryType.EXPENSES, icon = CategoryIcon(
                        icon = CategoryIcons.IC_EDUCATION,
                        color = CategoryColors.BLUE_T60
                    )
                ), date = LocalDate.now(), amount = 0.0, note = ""
            )
        )
    }

    override fun getFilterList(categoryType: CategoryType)
            : Flow<Resource<List<TransactionFilter>, TransactionFailure>> =
        flow {
            emit(Resource.Success(listOf(TransactionFilter.All)))
        }

    override fun getCategoriesSummary(
        categoryType: CategoryType,
        period: StatisticsPeriod
    ): Flow<Resource<CategoriesSummary, TransactionFailure>> =
        flow {
            emit(
                Resource.Success(
                    CategoriesSummary(
                        categories = listOf(),
                        totalAmount = 0.0
                    )
                )
            )
        }
}
