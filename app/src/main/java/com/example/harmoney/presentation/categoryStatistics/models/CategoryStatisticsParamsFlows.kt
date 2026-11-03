package com.example.harmoney.presentation.categoryStatistics.models

import com.example.harmoney.core.util.AsyncResult
import com.example.harmoney.domain.account.models.AccountFailure
import com.example.harmoney.domain.models.AccountScope
import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.domain.models.StatisticsPeriod
import kotlinx.coroutines.flow.Flow

data class CategoryStatisticsParamsFlows(
    val selectedAccountScopeFlow: Flow<AsyncResult<AccountScope, AccountFailure>>,
    val selectedCategoryTypeFlow: Flow<CategoryType>,
    val selectedPeriodFlow: Flow<StatisticsPeriod>,
)
