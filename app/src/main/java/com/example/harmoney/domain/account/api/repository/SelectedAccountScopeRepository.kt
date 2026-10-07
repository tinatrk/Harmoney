package com.example.harmoney.domain.account.api.repository

import kotlinx.coroutines.flow.Flow

interface SelectedAccountScopeRepository {
    fun getSelectedAccountScope(): Flow<Long>

    suspend fun saveSelectedAccountScope(accountScopeId: Long)
}
