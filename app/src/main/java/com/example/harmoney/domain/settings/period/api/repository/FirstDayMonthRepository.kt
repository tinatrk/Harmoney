package com.example.harmoney.domain.settings.period.api.repository

import kotlinx.coroutines.flow.Flow

interface FirstDayMonthRepository {
    fun getFirstDayMonth(): Flow<Int>

    suspend fun setFirstDayMonth(firstDay: Int)

    fun isFirstDayMonthCorrect(firstDay: Int): Boolean
}
