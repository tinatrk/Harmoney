package com.example.harmoney.domain.statistics.impl

import com.example.harmoney.core.util.Resource
import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.domain.models.Money
import com.example.harmoney.domain.models.StatisticsPeriod
import com.example.harmoney.domain.statistics.api.repository.StatisticsRepository
import com.example.harmoney.domain.statistics.api.useCase.GetCategoriesTotalUseCase
import com.example.harmoney.domain.transaction.models.TransactionFailure
import kotlinx.coroutines.flow.Flow

class GetCategoriesTotalUseCaseImpl(private val repository: StatisticsRepository) :
    GetCategoriesTotalUseCase {
    override fun execute(
        accountIds: List<Long>,
        categoryType: CategoryType,
        period: StatisticsPeriod
    ): Flow<Resource<List<Money>, TransactionFailure>> {
        return repository.getCategoriesTotal(accountIds, categoryType, period)
    }
}
