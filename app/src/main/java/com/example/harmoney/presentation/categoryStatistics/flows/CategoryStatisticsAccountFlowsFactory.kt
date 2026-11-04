package com.example.harmoney.presentation.categoryStatistics.flows

import com.example.harmoney.domain.account.api.useCase.GetAccountListUseCase
import com.example.harmoney.domain.account.api.useCase.GetAccountScopesUseCase
import com.example.harmoney.domain.account.api.useCase.GetAccountScopesWithBalanceUseCase
import com.example.harmoney.domain.account.api.useCase.SelectedAccountScopeInteractor
import com.example.harmoney.domain.models.Currency
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

class CategoryStatisticsAccountFlowsFactory(
    private val selectedAccountScopeInteractor: SelectedAccountScopeInteractor,
    private val getAccountListUseCase: GetAccountListUseCase,
    private val getAccountScopesUseCase: GetAccountScopesUseCase,
    private val getAccountScopesWithBalanceUseCase: GetAccountScopesWithBalanceUseCase,
) {
    fun create(
        reportingCurrencyFlow: Flow<Currency>,
        scope: CoroutineScope
    ): CategoryStatisticsAccountFlows {
        return CategoryStatisticsAccountFlows(
            selectedAccountScopeInteractor = selectedAccountScopeInteractor,
            getAccountListUseCase = getAccountListUseCase,
            getAccountScopesUseCase = getAccountScopesUseCase,
            reportingCurrencyFlow = reportingCurrencyFlow,
            getAccountScopesWithBalanceUseCase = getAccountScopesWithBalanceUseCase,
            scope = scope,
        )
    }
}
