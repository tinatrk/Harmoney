package com.example.harmoney.presentation.categoryStatistics.models

sealed interface CategoryStatisticsAction {
    data class NavigateToTransactionList(val categoryId: Long?) : CategoryStatisticsAction
    data object NavigateToTransaction : CategoryStatisticsAction
    data object NavigateToSettings : CategoryStatisticsAction
    data object NavigateToCategoryList : CategoryStatisticsAction
    data class NavigateToCurrency(val currencyId: Long) : CategoryStatisticsAction

    data object ShowChangeThemeError : CategoryStatisticsAction
}
