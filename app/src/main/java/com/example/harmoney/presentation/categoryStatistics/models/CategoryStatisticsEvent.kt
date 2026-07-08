package com.example.harmoney.presentation.categoryStatistics.models

import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.domain.models.Currency
import com.example.harmoney.domain.models.StatisticsPeriodType

sealed interface CategoryStatisticsEvent {
    data class OnTabClick(val categoryType: CategoryType) : CategoryStatisticsEvent
    data object OnSettingsIconClick : CategoryStatisticsEvent
    data object OnTransactionListIconClick : CategoryStatisticsEvent

    data class OnStatisticsPeriodClick(val newPeriodType: StatisticsPeriodType)
        : CategoryStatisticsEvent

    data class OnCategoryClick(val categoryId: Long) : CategoryStatisticsEvent
    data object OnFloatingButtonClick : CategoryStatisticsEvent

    data class OnChangeTheme(val isThemeDark: Boolean) : CategoryStatisticsEvent
    data object OnCategoryListClick : CategoryStatisticsEvent

    data object OnFirstDayMonthClick : CategoryStatisticsEvent
    data class OnFirstDayMonthTextChanged(val newText: String): CategoryStatisticsEvent
    data object OnFirstDayMonthDialogConfirm: CategoryStatisticsEvent
    data object OnFirstDayMonthDialogDismiss: CategoryStatisticsEvent

    data object OnCurrencySettingsClick: CategoryStatisticsEvent
    data class OnCurrencyChanged(val newCurrency: Currency) : CategoryStatisticsEvent
    data object OnCurrencyMenuDismiss: CategoryStatisticsEvent
}
