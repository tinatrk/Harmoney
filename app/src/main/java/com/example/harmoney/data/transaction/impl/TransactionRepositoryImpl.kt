package com.example.harmoney.data.transaction.impl

import androidx.sqlite.SQLiteException
import com.example.harmoney.core.util.Resource
import com.example.harmoney.data.converters.DateConverter
import com.example.harmoney.data.transaction.converter.CategoryStatisticsDbConverter
import com.example.harmoney.data.transaction.converter.TransactionDbConverter
import com.example.harmoney.data.transaction.dao.TransactionDao
import com.example.harmoney.domain.models.CategoriesSummary
import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.domain.models.Money
import com.example.harmoney.domain.models.StatisticsPeriod
import com.example.harmoney.domain.models.Transaction
import com.example.harmoney.domain.models.TransactionFilter
import com.example.harmoney.domain.models.TransactionsPerDay
import com.example.harmoney.domain.models.TransactionsSummary
import com.example.harmoney.domain.transaction.api.repository.TransactionRepository
import com.example.harmoney.domain.transaction.models.TransactionFailure
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import timber.log.Timber

class TransactionRepositoryImpl(
    private val transactionDao: TransactionDao,
    private val transactionDbConverter: TransactionDbConverter,
    private val categoryStatisticsDbConverter: CategoryStatisticsDbConverter,
    private val dateConverter: DateConverter
) : TransactionRepository {
    override suspend fun addTransaction(transaction: Transaction)
            : Resource<Unit, TransactionFailure> {
        return try {
            transactionDao.insertTransaction(transactionDbConverter.map(transaction))
            Resource.Success(Unit)
        } catch (e: SQLiteException) {
            Timber.e(e, "Error when inserting transaction")
            Resource.Error(TransactionFailure.DatabaseError)
        }
    }

    override suspend fun deleteTransaction(transaction: Transaction)
            : Resource<Unit, TransactionFailure> {
        return try {
            transactionDao.deleteTransaction(transactionDbConverter.map(transaction))
            Resource.Success(Unit)
        } catch (e: SQLiteException) {
            Timber.e(e, "Error when deleting transaction")
            Resource.Error(TransactionFailure.DatabaseError)
        }
    }

    override suspend fun updateTransaction(transaction: Transaction)
            : Resource<Unit, TransactionFailure> {
        return try {
            transactionDao.updateTransaction(transactionDbConverter.map(transaction))
            Resource.Success(Unit)
        } catch (e: SQLiteException) {
            Timber.e(e, "Error when updating transaction")
            Resource.Error(TransactionFailure.DatabaseError)
        }
    }

    override suspend fun getTransaction(transactionId: Long)
            : Resource<Transaction, TransactionFailure> {
        return try {
            val transaction = transactionDao.getTransaction(transactionId)
            if (transaction != null) {
                Resource.Success(transactionDbConverter.map(transaction))
            } else {
                Resource.Error(TransactionFailure.BadRequest)
            }
        } catch (e: SQLiteException) {
            Timber.e(e, "Error getting transaction")
            Resource.Error(TransactionFailure.DatabaseError)
        }
    }

    override fun getTransactionsSummary(
        categoryType: CategoryType,
        period: StatisticsPeriod,
        filter: TransactionFilter
    ): Flow<Resource<TransactionsSummary, TransactionFailure>> {
        val transactionsWithCategoryFlow = transactionDao.getTransactionListWithCategory(
            categoryTypeId = categoryType.id,
            firstDayOfPeriodMillis = dateConverter.dateToMillis(period.firstDay),
            lastDayOfPeriodMillis = dateConverter.dateToMillis(period.lastDay),
            categoryId = when (filter) {
                is TransactionFilter.All -> null
                is TransactionFilter.Category -> filter.id
            }
        )

        val totalAmountFlow = transactionDao.getTotalAmount(
            categoryTypeId = categoryType.id,
            firstDayOfPeriodMillis = dateConverter.dateToMillis(period.firstDay),
            lastDayOfPeriodMillis = dateConverter.dateToMillis(period.lastDay),
            categoryId = when (filter) {
                is TransactionFilter.All -> null
                is TransactionFilter.Category -> filter.id
            }
        )

        return combine(
            transactionsWithCategoryFlow, totalAmountFlow
        ) { transactionsWithCategory, totalAmount ->
            TransactionsSummary(
                days = transactionsWithCategory.groupBy { transactionWithCategory ->
                    dateConverter.millisToDate(transactionWithCategory.transaction.dateMillis)
                }.map { (date, items) ->
                    TransactionsPerDay(
                        date = date,
                        transactions = items.map { item ->
                            transactionDbConverter.map(item)
                        },
                        totalAmount = Money(
                            minorUnits = items.sumOf { item -> item.transaction.amount }
                        )
                    )
                }.sortedByDescending { it.date },

                totalAmount = Money(totalAmount)
            )
        }
            .map<TransactionsSummary, Resource<TransactionsSummary, TransactionFailure>> {
                Resource.Success(it)
            }
            .catch { e ->
                Timber.e(e, "Error getting transactions summary")
                emit(Resource.Error(TransactionFailure.DatabaseError))
            }
    }

    override fun getCategoriesSummary(
        categoryType: CategoryType,
        period: StatisticsPeriod
    ): Flow<Resource<CategoriesSummary, TransactionFailure>> =
        transactionDao.getCategoryStatisticsList(
            categoryTypeId = categoryType.id,
            firstDayOfPeriodMillis = dateConverter.dateToMillis(period.firstDay),
            lastDayOfPeriodMillis = dateConverter.dateToMillis(period.lastDay)
        ).map { categoriesDB ->
            val categories = categoryStatisticsDbConverter.map(categoriesDB)
            val totalAmount = categories.sumOf { it.totalAmount.minorUnits }
            Resource.Success(
                CategoriesSummary(
                    categories = categories,
                    totalAmount = Money(totalAmount)
                )
            ) as Resource<CategoriesSummary, TransactionFailure>
        }.catch { e ->
            Timber.e(e, "Error getting categories summary")
            when (e) {
                is SQLiteException -> emit(Resource.Error(TransactionFailure.DatabaseError))
                else -> throw e
            }
        }
}
