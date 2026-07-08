package com.example.harmoney.core.session

import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.domain.models.StatisticsPeriodType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class SessionStateHolder {
    private val _state = MutableStateFlow(SessionState())
    val state = _state.asStateFlow()

    fun setPeriodType(periodType: StatisticsPeriodType) =
        _state.update { it.copy(statisticsPeriodType = periodType) }

    fun setCategoryType(categoryType: CategoryType) =
        _state.update { it.copy(categoryType = categoryType) }
}
