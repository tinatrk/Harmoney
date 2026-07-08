package com.example.harmoney.domain.settings.period.api.useCase

import kotlinx.coroutines.flow.Flow

interface FirstDayMonthInteractor {
    fun getFirstDayMonth(): Flow<Int>

    suspend fun setFirstDayMonth(firstDay: Int)

    fun isFirstDayMonthCorrect(firstDay: Int): Boolean
}
