package com.example.harmoney.domain.settings.period.impl

import com.example.harmoney.domain.models.StatisticsPeriod
import com.example.harmoney.domain.settings.period.api.repository.FirstDayMonthRepository
import com.example.harmoney.domain.settings.period.api.useCase.GetStatisticsPeriodsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetStatisticsPeriodsUseCaseImpl(
    private val repository: FirstDayMonthRepository,
    private val calculator: StatisticsPeriodCalculator
) : GetStatisticsPeriodsUseCase {
    override fun invoke(): Flow<List<StatisticsPeriod>> {
        return repository.getFirstDayMonth().map { firstDay ->
            calculator.createPeriods(firstDay)
        }
    }
}
