package com.example.harmoney.domain.account.api.useCase

import kotlinx.coroutines.flow.Flow

interface SelectedAccountScopeInteractor {
    fun getSelectedScope(): Flow<Long>

    suspend fun saveSelectedScope(accountScopeId: Long)
}
