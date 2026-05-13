package com.example.harmoney.presentation.categoryStatistics.models

import com.example.harmoney.domain.models.CategoryType

sealed interface CategoryStatisticsEvent {
    data class OnTabClick(val categoryType: CategoryType) : CategoryStatisticsEvent
    data object OnSettingsIconClick : CategoryStatisticsEvent
    data object OnTransactionListIconClick : CategoryStatisticsEvent
    data class OnCategoryClick(val categoryId: Long) : CategoryStatisticsEvent
    data object OnFloatingButtonClick : CategoryStatisticsEvent
    data object OnChangeTheme : CategoryStatisticsEvent
    data object OnFirstDayMonthClick : CategoryStatisticsEvent
    data object OnCategoryListClick : CategoryStatisticsEvent
    data class OnStatisticsPeriodClick(val newPeriodId: Long) : CategoryStatisticsEvent
    // логика работы с валютой
}
