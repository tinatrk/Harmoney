package com.example.harmoney.domain.settings.period.impl

import com.example.harmoney.domain.settings.period.api.repository.FirstDayMonthRepository
import com.example.harmoney.domain.settings.period.api.useCase.FirstDayMonthInteractor
import kotlinx.coroutines.flow.Flow

class FirstDayMonthInteractorImpl(private val repository: FirstDayMonthRepository) :
    FirstDayMonthInteractor {
    override fun getFirstDayMonth(): Flow<Int> {
        return repository.getFirstDayMonth()
    }

    override suspend fun setFirstDayMonth(firstDay: Int) {
        repository.setFirstDayMonth(firstDay)
    }

    override fun isFirstDayMonthCorrect(firstDay: Int): Boolean {
        return repository.isFirstDayMonthCorrect(firstDay)
    }
}
