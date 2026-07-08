package com.example.harmoney.data.settings.firstDayMonth.impl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.example.harmoney.domain.settings.period.api.repository.FirstDayMonthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FirstDayMonthRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) :
    FirstDayMonthRepository {

    override fun getFirstDayMonth(): Flow<Int> =
        dataStore.data.map { prefs ->
            prefs[FIRST_DAY_MONTH_KEY] ?: MIN_FIRST_DAY_MONTH
        }

    override suspend fun setFirstDayMonth(firstDay: Int) {
        if (isFirstDayMonthCorrect(firstDay)) {
            dataStore.edit { prefs ->
                prefs[FIRST_DAY_MONTH_KEY] = firstDay
            }
        }
    }

    override fun isFirstDayMonthCorrect(firstDay: Int): Boolean {
        return firstDay in MIN_FIRST_DAY_MONTH..MAX_FIRST_DAY_MONTH
    }

    companion object {
        private const val MIN_FIRST_DAY_MONTH = 1
        private const val MAX_FIRST_DAY_MONTH = 28
        private val FIRST_DAY_MONTH_KEY = intPreferencesKey("first_day_month")
    }
}
