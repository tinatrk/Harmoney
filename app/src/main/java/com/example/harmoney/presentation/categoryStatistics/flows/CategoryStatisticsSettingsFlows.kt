package com.example.harmoney.presentation.categoryStatistics.flows

import com.example.harmoney.core.session.SessionStateHolder
import com.example.harmoney.domain.models.CategoryType
import com.example.harmoney.domain.models.Currency
import com.example.harmoney.domain.models.StatisticsPeriodType
import com.example.harmoney.domain.settings.currency.api.useCase.ReportingCurrencyInteractor
import com.example.harmoney.domain.settings.period.api.useCase.FirstDayMonthInteractor
import com.example.harmoney.domain.settings.period.api.useCase.GetStatisticsPeriodsUseCase
import com.example.harmoney.presentation.categoryStatistics.models.CategoryStatisticsSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class CategoryStatisticsSettingsFlows(
    private val sessionStateHolder: SessionStateHolder,
    private val reportingCurrencyInteractor: ReportingCurrencyInteractor,
    private val firstDayMonthInteractor: FirstDayMonthInteractor,
    getStatisticsPeriodsUseCase: GetStatisticsPeriodsUseCase,
) {
    private val sessionFlow = sessionStateHolder.state

    val selectedCategoryType: CategoryType get() = sessionStateHolder.state.value.categoryType

    val selectedCategoryTypeFlow = sessionFlow.map { it.categoryType }.distinctUntilChanged()

    val periodsFlow = getStatisticsPeriodsUseCase().distinctUntilChanged()

    val reportingCurrencyFlow = reportingCurrencyInteractor
        .getReportingCurrency().distinctUntilChanged()

    val firstDayMonthFlow = firstDayMonthInteractor
        .getFirstDayMonth()
        .distinctUntilChanged()

    val selectedPeriodFlow =
        combine(sessionFlow, periodsFlow) { session, periods ->
            periods.first { it.type == session.statisticsPeriodType }
        }.distinctUntilChanged()

    val screenSettingsFlow: Flow<CategoryStatisticsSettings> =
        combine(
            sessionFlow,
            periodsFlow,
            selectedPeriodFlow,
            reportingCurrencyFlow,
            firstDayMonthFlow,
        ) { session, periods, selectedPeriod, reportingCurrency, firstDayMonth ->
            CategoryStatisticsSettings(
                selectedCategoryType = session.categoryType,
                periods = periods,
                selectedPeriod = selectedPeriod,
                reportingCurrency = reportingCurrency,
                firstDayMonth = firstDayMonth
            )
        }.distinctUntilChanged()

    fun saveSelectedCategoryType(newCategoryType: CategoryType) {
        sessionStateHolder.setCategoryType(newCategoryType)
    }

    fun saveSelectedStatisticsPeriod(newPeriodType: StatisticsPeriodType) {
        sessionStateHolder.setPeriodType(newPeriodType)
    }

    suspend fun saveReportingCurrency(currency: Currency) {
        reportingCurrencyInteractor.saveReportingCurrency(currency)
    }

    suspend fun updateFirstDayMonth(firstDayMonth: Int) {
        firstDayMonthInteractor.setFirstDayMonth(firstDayMonth)
    }
}
