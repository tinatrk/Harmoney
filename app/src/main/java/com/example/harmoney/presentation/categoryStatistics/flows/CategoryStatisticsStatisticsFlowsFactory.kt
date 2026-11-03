package com.example.harmoney.presentation.categoryStatistics.flows

import com.example.harmoney.domain.models.Currency
import com.example.harmoney.domain.statistics.api.useCase.GetCategoriesTotalUseCase
import com.example.harmoney.domain.statistics.api.useCase.GetCategoryStatisticsUseCase
import com.example.harmoney.domain.statistics.api.useCase.PrepareCategoryStatisticsInteractor
import com.example.harmoney.presentation.categoryStatistics.models.CategoryStatisticsParamsFlows
import kotlinx.coroutines.flow.Flow

class CategoryStatisticsStatisticsFlowsFactory(
    private val getCategoryStatisticsUseCase: GetCategoryStatisticsUseCase,
    private val getCategoriesTotalUseCase: GetCategoriesTotalUseCase,
    private val prepareCategoryStatisticsInteractor: PrepareCategoryStatisticsInteractor,
) {
    fun create(
        categoryStatisticsParamsFlows: CategoryStatisticsParamsFlows,
        reportingCurrencyFlow: Flow<Currency>,
    ): CategoryStatisticsStatisticsFlows {
        return CategoryStatisticsStatisticsFlows(
            categoryStatisticsParamsFlows = categoryStatisticsParamsFlows,
            reportingCurrencyFlow = reportingCurrencyFlow,
            getCategoryStatisticsUseCase = getCategoryStatisticsUseCase,
            getCategoriesTotalUseCase = getCategoriesTotalUseCase,
            prepareCategoryStatisticsInteractor = prepareCategoryStatisticsInteractor,
        )
    }
}
