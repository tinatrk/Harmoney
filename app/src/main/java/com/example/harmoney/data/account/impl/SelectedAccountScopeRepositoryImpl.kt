package com.example.harmoney.data.account.impl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import com.example.harmoney.domain.account.api.repository.SelectedAccountScopeRepository
import com.example.harmoney.domain.models.AccountGroupType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SelectedAccountScopeRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : SelectedAccountScopeRepository {
    override fun getSelectedAccountScope(): Flow<Long> =
        dataStore.data.map { prefs ->
            prefs[SELECTED_ACCOUNT_SCOPE_ID_KEY] ?: AccountGroupType.ALL.id
        }

    override suspend fun saveSelectedAccountScope(accountScopeId: Long) {
        dataStore.edit { prefs ->
            prefs[SELECTED_ACCOUNT_SCOPE_ID_KEY] = accountScopeId
        }
    }

    companion object {
        private val SELECTED_ACCOUNT_SCOPE_ID_KEY =
            longPreferencesKey("selected_account_scope_id")
    }
}
