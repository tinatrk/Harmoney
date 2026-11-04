package com.example.harmoney.presentation.categoryStatistics.flows

import com.example.harmoney.core.util.AsyncResult
import com.example.harmoney.core.util.Resource
import com.example.harmoney.core.util.mapSuccess
import com.example.harmoney.domain.account.models.AccountFailure
import com.example.harmoney.domain.models.AccountScope
import com.example.harmoney.domain.models.Currency
import com.example.harmoney.domain.statistics.api.useCase.GetCategoriesTotalUseCase
import com.example.harmoney.domain.statistics.api.useCase.GetCategoryStatisticsUseCase
import com.example.harmoney.domain.statistics.api.useCase.PrepareCategoryStatisticsInteractor
import com.example.harmoney.domain.statistics.models.RawStatistics
import com.example.harmoney.domain.statistics.models.StatisticsData
import com.example.harmoney.domain.statistics.models.StatisticsFailure
import com.example.harmoney.presentation.categoryStatistics.models.CategoryStatisticsParamsFlows
import com.example.harmoney.presentation.categoryStatistics.models.StatisticsParams
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class CategoryStatisticsStatisticsFlows(
    categoryStatisticsParamsFlows: CategoryStatisticsParamsFlows,
    reportingCurrencyFlow: Flow<Currency>,
    private val getCategoryStatisticsUseCase: GetCategoryStatisticsUseCase,
    private val getCategoriesTotalUseCase: GetCategoriesTotalUseCase,
    private val prepareCategoryStatisticsInteractor: PrepareCategoryStatisticsInteractor,
) {
    private fun AccountScope.toIds(): List<Long> = accounts.map { it.id }

    private val statisticsParamsFlow: Flow<AsyncResult<StatisticsParams, AccountFailure>> =
        combine(
            categoryStatisticsParamsFlows.selectedAccountScopeFlow,
            categoryStatisticsParamsFlows.selectedCategoryTypeFlow,
            categoryStatisticsParamsFlows.selectedPeriodFlow,
        ) { scopeResult, categoryType, period ->
            when (scopeResult) {
                is AsyncResult.Loading -> AsyncResult.Loading

                is AsyncResult.Error -> AsyncResult.Error(scopeResult.error)

                is AsyncResult.Success -> AsyncResult.Success(
                    StatisticsParams(
                        accountScope = scopeResult.data,
                        categoryType = categoryType,
                        period = period
                    )
                )
            }
        }.distinctUntilChanged()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val rawStatisticsFlow:
            Flow<AsyncResult<Pair<StatisticsParams, RawStatistics>, StatisticsFailure>> =
        statisticsParamsFlow
            .flatMapLatest { paramsResult ->
                when (paramsResult) {
                    is AsyncResult.Loading -> flowOf(AsyncResult.Loading)

                    is AsyncResult.Error -> flowOf(
                        AsyncResult.Error(
                            StatisticsFailure.Account(
                                paramsResult.error
                            )
                        )
                    )

                    is AsyncResult.Success -> {
                        val params = paramsResult.data

                        getRawStatisticsFlow(params)
                            .map { rawResult ->
                                rawResult.mapSuccess { raw -> params to raw }
                            }
                    }
                }
            }.distinctUntilChanged()

    private fun getRawStatisticsFlow(params: StatisticsParams):
            Flow<AsyncResult<RawStatistics, StatisticsFailure>> =
        combine(
            getCategoryStatisticsUseCase(
                accountIds = params.accountScope.toIds(),
                categoryType = params.categoryType,
                period = params.period
            ).distinctUntilChanged(),
            getCategoriesTotalUseCase.execute(
                accountIds = params.accountScope.toIds(),
                categoryType = params.categoryType,
                period = params.period
            ).distinctUntilChanged()
        ) { categoriesResource, totalResource ->
            when {
                categoriesResource is Resource.Error -> AsyncResult.Error(
                    StatisticsFailure.Transaction(categoriesResource.error)
                )

                totalResource is Resource.Error -> AsyncResult.Error(
                    StatisticsFailure.Transaction(totalResource.error)
                )

                categoriesResource is Resource.Success
                        && totalResource is Resource.Success -> AsyncResult.Success(
                    RawStatistics(
                        categories = categoriesResource.data,
                        total = totalResource.data
                    )
                )

                else -> error(UNEXPECTED_RESOURCE_STATE_ERROR)
            }
        }.onStart {
            emit(AsyncResult.Loading)
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    val statisticsFlow: Flow<AsyncResult<StatisticsData, StatisticsFailure>> =
        rawStatisticsFlow.flatMapLatest { rawResult ->
            when (rawResult) {
                is AsyncResult.Loading -> flowOf(AsyncResult.Loading)

                is AsyncResult.Error -> flowOf(AsyncResult.Error(rawResult.error))

                is AsyncResult.Success -> {
                    val (params, raw) = rawResult.data

                    when (val scope = params.accountScope) {
                        is AccountScope.Single -> flowOf(
                            AsyncResult.Success(
                                prepareCategoryStatisticsInteractor.prepareOriginal(
                                    raw = raw, currency = scope.account.currency
                                )
                            )
                        )

                        is AccountScope.Group -> {
                            reportingCurrencyFlow.flatMapLatest { reportingCurrency ->
                                flow {
                                    emit(AsyncResult.Loading)

                                    val result =
                                        prepareCategoryStatisticsInteractor.prepareConverted(
                                            raw = raw, targetCurrency = reportingCurrency
                                        )

                                    emit(
                                        when (result) {
                                            is Resource.Success -> {
                                                AsyncResult.Success(result.data)
                                            }

                                            is Resource.Error -> AsyncResult.Error(
                                                StatisticsFailure
                                                    .ExchangeRate(result.error)
                                            )
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

    private companion object {
        const val UNEXPECTED_RESOURCE_STATE_ERROR = "Unexpected Resource state"
    }
}
