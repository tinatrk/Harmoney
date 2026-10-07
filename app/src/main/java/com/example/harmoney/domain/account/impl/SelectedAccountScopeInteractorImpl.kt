package com.example.harmoney.domain.account.impl

import com.example.harmoney.domain.account.api.repository.SelectedAccountScopeRepository
import com.example.harmoney.domain.account.api.useCase.SelectedAccountScopeInteractor
import kotlinx.coroutines.flow.Flow

class SelectedAccountScopeInteractorImpl(
    private val repository: SelectedAccountScopeRepository,
) : SelectedAccountScopeInteractor {
    override fun getSelectedScope(): Flow<Long> {
        return repository.getSelectedAccountScope()
    }

    override suspend fun saveSelectedScope(accountScopeId: Long) {
        repository.saveSelectedAccountScope(accountScopeId)
    }
}
