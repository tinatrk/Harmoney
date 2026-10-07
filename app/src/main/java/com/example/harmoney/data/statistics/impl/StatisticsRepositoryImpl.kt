package com.example.harmoney.data.statistics.impl

import com.example.harmoney.core.util.Resource
import com.example.harmoney.data.converters.DateConverter
import com.example.harmoney.data.core.bd.dto.TotalMoneyDto
import com.example.harmoney.data.statistics.converter.CategoryStatisticsDbConverter
import com.example.harmoney.data.statistics.dao.StatisticsDao
import com.example.harmoney.domain.models.CategoriesSummary
import com.example.harmoney.domain.models.CategoryStatistics
import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.domain.models.Currency
import com.example.harmoney.domain.models.Money
import com.example.harmoney.domain.models.MoneyAmount
import com.example.harmoney.domain.models.StatisticsPeriod
import com.example.harmoney.domain.statistics.api.repository.StatisticsRepository
import com.example.harmoney.domain.transaction.models.TransactionFailure
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import timber.log.Timber

class StatisticsRepositoryImpl(
    private val statisticsDao: StatisticsDao,
    private val dateConverter: DateConverter,
    private val categoryStatisticsDbConverter: CategoryStatisticsDbConverter,
) : StatisticsRepository {
    override fun getCategoriesSummary(
        accountIds: List<Long>,
        categoryType: CategoryType,
        period: StatisticsPeriod
    ): Flow<Resource<CategoriesSummary, TransactionFailure>> {

        val firstDayMillis = dateConverter.dateToMillis(period.firstDay)
        val lastDayMillis = dateConverter.dateToMillis(period.lastDay)

        val categoryStatisticsListFlow =
            statisticsDao.observeCategoryStatisticsList(
                accountIds = accountIds,
                categoryTypeId = categoryType.id,
                firstDayOfPeriodMillis = firstDayMillis,
                lastDayOfPeriodMillis = lastDayMillis
            ).map { rows ->
                rows.groupBy { it.id }
                    .map { (_, categoryRows) ->
                        val category = categoryRows.first()
                        val totals = categoryRows.map { row ->
                            TotalMoneyDto(
                                moneyAmountMinorUnits = row.moneyAmountMinorUnits,
                                currencyId = row.currencyId
                            )
                        }

                        categoryStatisticsDbConverter.map(category, totals)
                    }
            }

        val totalListFlow =
            statisticsDao.observeTotalAmountByCategoryType(
            accountIds = accountIds,
            categoryTypeId = categoryType.id,
            firstDayOfPeriodMillis = firstDayMillis,
            lastDayOfPeriodMillis = lastDayMillis,
        ).map { totalList ->
            totalList.map { total ->
                Money(
                    amount = MoneyAmount(total.moneyAmountMinorUnits),
                    currency = Currency.fromId(total.currencyId)
                )
            }
        }

        return combine(
            categoryStatisticsListFlow,
            totalListFlow
        ) { categoryStatisticsList, totalList ->
            CategoriesSummary(categories = categoryStatisticsList, totalAmounts = totalList)
        }.map<CategoriesSummary, Resource<CategoriesSummary, TransactionFailure>> {
            Resource.Success(it)
        }.catch { e ->
            Timber.e(e, "Error getting categories summary")
            emit(Resource.Error(TransactionFailure.DatabaseError))
        }
    }

    override fun getCategoryStatistics(
        accountIds: List<Long>,
        categoryType: CategoryType,
        period: StatisticsPeriod
    ): Flow<Resource<List<CategoryStatistics>, TransactionFailure>> {
        val firstDayMillis = dateConverter.dateToMillis(period.firstDay)
        val lastDayMillis = dateConverter.dateToMillis(period.lastDay)

        return statisticsDao.observeCategoryStatisticsList(
            accountIds = accountIds,
            categoryTypeId = categoryType.id,
            firstDayOfPeriodMillis = firstDayMillis,
            lastDayOfPeriodMillis = lastDayMillis
        ).map { rows ->
            rows.groupBy { it.id }
                .map { (_, categoryRows) ->
                    val category = categoryRows.first()
                    val totals = categoryRows.map { row ->
                        TotalMoneyDto(
                            moneyAmountMinorUnits = row.moneyAmountMinorUnits,
                            currencyId = row.currencyId
                        )
                    }

                    categoryStatisticsDbConverter.map(category, totals)
                }
        }.map<List<CategoryStatistics>, Resource<List<CategoryStatistics>, TransactionFailure>> {
            Resource.Success(it)
        }.catch { e ->
            Timber.e(e, "Error observing category statistics list")
            emit(Resource.Error(TransactionFailure.DatabaseError))
        }
    }

    override fun getCategoriesTotal(
        accountIds: List<Long>,
        categoryType: CategoryType,
        period: StatisticsPeriod
    ): Flow<Resource<List<Money>, TransactionFailure>> {
        val firstDayMillis = dateConverter.dateToMillis(period.firstDay)
        val lastDayMillis = dateConverter.dateToMillis(period.lastDay)

        return statisticsDao.observeTotalAmountByCategoryType(
            accountIds = accountIds,
            categoryTypeId = categoryType.id,
            firstDayOfPeriodMillis = firstDayMillis,
            lastDayOfPeriodMillis = lastDayMillis,
        ).map { totalList ->
            totalList.map { total ->
                Money(
                    amount = MoneyAmount(total.moneyAmountMinorUnits),
                    currency = Currency.fromId(total.currencyId)
                )
            }
        }.map<List<Money>, Resource<List<Money>, TransactionFailure>> {
            Resource.Success(it)
        }.catch { e ->
            Timber.e(e, "Error observing total amount by category type")
            emit(Resource.Error(TransactionFailure.DatabaseError))
        }
    }
}
