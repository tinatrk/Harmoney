package com.example.harmoney.data.transaction.impl

import androidx.sqlite.SQLiteException
import com.example.harmoney.core.util.Resource
import com.example.harmoney.data.converters.DateConverter
import com.example.harmoney.data.statistics.dao.StatisticsDao
import com.example.harmoney.data.transaction.converter.TransactionDBConverter
import com.example.harmoney.data.transaction.dao.TransactionDao
import com.example.harmoney.domain.models.Account
import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.domain.models.Currency
import com.example.harmoney.domain.models.Money
import com.example.harmoney.domain.models.MoneyAmount
import com.example.harmoney.domain.models.StatisticsPeriod
import com.example.harmoney.domain.models.Transaction
import com.example.harmoney.domain.models.TransactionFilter
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
    private val transactionDbConverter: TransactionDBConverter,
    private val dateConverter: DateConverter,
    private val statisticsDao: StatisticsDao,
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
        accounts: List<Account>,
        categoryType: CategoryType,
        period: StatisticsPeriod,
        filter: TransactionFilter
    ): Flow<Resource<TransactionsSummary, TransactionFailure>> {
        val accountIds = accounts.map { it.id }
        val firstDayMillis = dateConverter.dateToMillis(period.firstDay)
        val lastDayMillis = dateConverter.dateToMillis(period.lastDay)

        val transactionsFlow = transactionDao.getTransactionList(
            accountIds = accountIds,
            categoryTypeId = categoryType.id,
            firstDayOfPeriodMillis = firstDayMillis,
            lastDayOfPeriodMillis = lastDayMillis,
            categoryId = when (filter) {
                is TransactionFilter.All -> null
                is TransactionFilter.Category -> filter.id
            }
        )

        val totalsAmountFlow = statisticsDao.observeTotalAmountByCategoryType(
            accountIds = accountIds,
            categoryTypeId = categoryType.id,
            firstDayOfPeriodMillis = firstDayMillis,
            lastDayOfPeriodMillis = lastDayMillis,
            categoryId = when (filter) {
                is TransactionFilter.All -> null
                is TransactionFilter.Category -> filter.id
            }
        )

        return combine(
            transactionsFlow, totalsAmountFlow
        ) { transactions, totalsAmount ->
            TransactionsSummary(
                transactions = transactionDbConverter.map(transactions),
                totalAmounts = totalsAmount.map { total ->
                    Money(
                        amount = MoneyAmount(total.moneyAmountMinorUnits),
                        currency = Currency.fromId(total.currencyId)
                    )
                }
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
}
