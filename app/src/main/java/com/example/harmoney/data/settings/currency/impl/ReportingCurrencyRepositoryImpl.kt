package com.example.harmoney.data.settings.currency.impl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import com.example.harmoney.domain.models.Currency
import com.example.harmoney.domain.settings.currency.api.repository.ReportingCurrencyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ReportingCurrencyRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : ReportingCurrencyRepository {
    override fun getReportingCurrency(): Flow<Currency> =
        dataStore.data.map { prefs ->
            Currency.fromId(prefs[REPORTING_CURRENCY_ID_KEY] ?: Currency.RUB.id)
        }

    override suspend fun setReportingCurrency(currency: Currency) {
        dataStore.edit { prefs ->
            prefs[REPORTING_CURRENCY_ID_KEY] = currency.id
        }
    }

    companion object {
        private val REPORTING_CURRENCY_ID_KEY =
            longPreferencesKey("reporting_currency_id")
    }
}
