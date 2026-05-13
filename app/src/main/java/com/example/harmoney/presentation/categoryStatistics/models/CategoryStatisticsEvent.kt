package com.example.harmoney.presentation.categoryStatistics.models

import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.domain.models.Currency

sealed interface CategoryStatisticsEvent {
    data class OnTabClick(val categoryType: CategoryType) : CategoryStatisticsEvent
    data object OnSettingsIconClick : CategoryStatisticsEvent
    data object OnTransactionListIconClick : CategoryStatisticsEvent
    data class OnCategoryClick(val categoryId: Long) : CategoryStatisticsEvent
    data object OnFloatingButtonClick : CategoryStatisticsEvent
    data object OnChangeTheme : CategoryStatisticsEvent
    data object OnCategoryListClick : CategoryStatisticsEvent
    data class OnStatisticsPeriodClick(val newPeriodId: Long) : CategoryStatisticsEvent

    data object OnFirstDayMonthClick : CategoryStatisticsEvent
    data class OnFirstDayMonthTextChanged(val newText: String): CategoryStatisticsEvent
    data object OnFirstDayMonthDialogConfirm: CategoryStatisticsEvent
    data object OnFirstDayMonthDialogDismiss: CategoryStatisticsEvent

    data class OnCurrencyChanged(val newCurrency: Currency) : CategoryStatisticsEvent
}
