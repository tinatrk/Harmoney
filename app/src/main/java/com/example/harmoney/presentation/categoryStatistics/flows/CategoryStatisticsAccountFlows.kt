package com.example.harmoney.presentation.categoryStatistics.flows

import com.example.harmoney.core.util.AsyncResult
import com.example.harmoney.core.util.Resource
import com.example.harmoney.domain.account.api.useCase.GetAccountListUseCase
import com.example.harmoney.domain.account.api.useCase.GetAccountScopesUseCase
import com.example.harmoney.domain.account.api.useCase.GetAccountScopesWithBalanceInteractor
import com.example.harmoney.domain.account.api.useCase.SelectedAccountScopeInteractor
import com.example.harmoney.domain.account.models.AccountFailure
import com.example.harmoney.domain.account.models.AccountScopeWithBalance
import com.example.harmoney.domain.models.Account
import com.example.harmoney.domain.models.AccountGroupType
import com.example.harmoney.domain.models.AccountScope
import com.example.harmoney.domain.models.Currency
import com.example.harmoney.domain.models.SortOption
import com.example.harmoney.domain.statistics.models.StatisticsFailure
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.shareIn

class CategoryStatisticsAccountFlows(
    private val selectedAccountScopeInteractor: SelectedAccountScopeInteractor,
    private val getAccountListUseCase: GetAccountListUseCase,
    private val getAccountScopesUseCase: GetAccountScopesUseCase,
    private val reportingCurrencyFlow: Flow<Currency>,
    private val getAccountScopesWithBalanceInteractor: GetAccountScopesWithBalanceInteractor,
    private val scope: CoroutineScope,
) {
    private val selectedAccountScopeIdFlow =
        selectedAccountScopeInteractor.getSelectedScope().distinctUntilChanged()

    private val accountsFlow: SharedFlow<AsyncResult<List<Account>, AccountFailure>> =
        getAccountListUseCase.execute(SortOption.TIME_CREATED)
            .distinctUntilChanged()
            .map { resource ->
                when (resource) {
                    is Resource.Success -> AsyncResult.Success(resource.data)

                    is Resource.Error -> AsyncResult.Error(resource.error)
                }
            }
            .onStart { emit(AsyncResult.Loading) }
            .shareIn(
                scope = scope,
                started = SharingStarted.WhileSubscribed(5_000),
                replay = 1,
            )

    private val accountScopesFlow: Flow<AsyncResult<List<AccountScope>, AccountFailure>> =
        accountsFlow
            .map { accountsResult ->
                when (accountsResult) {
                    is AsyncResult.Loading -> AsyncResult.Loading

                    is AsyncResult.Error ->
                        AsyncResult.Error(accountsResult.error)

                    is AsyncResult.Success -> AsyncResult.Success(
                        getAccountScopesUseCase.execute(
                            accountsResult.data
                        )
                    )
                }
            }.distinctUntilChanged()
            .shareIn(
                scope = scope,
                started = SharingStarted.WhileSubscribed(5_000),
                replay = 1
            )

    val selectedAccountScopeFlow: Flow<AsyncResult<AccountScope, AccountFailure>> =
        combine(
            selectedAccountScopeIdFlow, accountScopesFlow
        ) { selectedScopeId, scopesResult ->
            when (scopesResult) {
                is AsyncResult.Loading -> AsyncResult.Loading

                is AsyncResult.Error -> AsyncResult.Error(scopesResult.error)

                is AsyncResult.Success -> {
                    val scopes = scopesResult.data
                    val selectedScope = scopes.firstOrNull { scope -> scope.id == selectedScopeId }
                        ?: scopes.firstOrNull { scope ->
                            scope.id == AccountGroupType.NOT_ARCHIVED.id
                        }
                        ?: scopes.first { scope -> scope.id == AccountGroupType.ALL.id }

                    AsyncResult.Success(selectedScope)
                }
            }
        }.distinctUntilChanged()
            .shareIn(
                scope = scope,
                started = SharingStarted.WhileSubscribed(5_000),
                replay = 1
            )

    @OptIn(ExperimentalCoroutinesApi::class)
    val accountScopesWithBalanceFlow
            : Flow<AsyncResult<List<AccountScopeWithBalance>, StatisticsFailure>> =
        combine(accountScopesFlow, reportingCurrencyFlow)
        { scopesResult, reportingCurrency ->
            scopesResult to reportingCurrency
        }.flatMapLatest { (scopesResult, reportingCurrency) ->
            when (scopesResult) {
                is AsyncResult.Loading -> flowOf(AsyncResult.Loading)

                is AsyncResult.Error -> flowOf(
                    AsyncResult.Error(
                        StatisticsFailure.Account(
                            scopesResult.error
                        )
                    )
                )

                is AsyncResult.Success -> flow {
                    emit(AsyncResult.Loading)
                    when (val result = getAccountScopesWithBalanceInteractor.execute(
                        scopes = scopesResult.data, targetCurrency = reportingCurrency
                    )) {
                        is Resource.Error -> {
                            emit(
                                AsyncResult.Error(
                                    StatisticsFailure.ExchangeRate(result.error)
                                )
                            )
                        }

                        is Resource.Success -> emit(AsyncResult.Success(result.data))
                    }
                }
            }
        }

    suspend fun saveSelectedAccountScope(newScopeId: Long) {
        selectedAccountScopeInteractor.saveSelectedScope(newScopeId)
    }
}
