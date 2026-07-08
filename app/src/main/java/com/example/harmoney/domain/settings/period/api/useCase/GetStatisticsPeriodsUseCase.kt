package com.example.harmoney.domain.settings.period.api.useCase

import com.example.harmoney.domain.models.StatisticsPeriod
import kotlinx.coroutines.flow.Flow

interface GetStatisticsPeriodsUseCase {
    operator fun invoke(): Flow<List<StatisticsPeriod>>
}
