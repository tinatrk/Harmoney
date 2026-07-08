package com.example.harmoney.presentation.sharedViewModel

import androidx.lifecycle.ViewModel
import com.example.harmoney.domain.models.StatisticsPeriodType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SharedStatisticsPeriodViewModel : ViewModel() {
    private val _selectedStatisticsPeriodType = MutableStateFlow(StatisticsPeriodType.CURRENT_MONTH)
    val selectedStatisticsPeriodType: StateFlow<StatisticsPeriodType> =
        _selectedStatisticsPeriodType.asStateFlow()

    fun statisticsPeriodChanged(newStatisticsPeriodType: StatisticsPeriodType) {
        if (selectedStatisticsPeriodType.value.id != newStatisticsPeriodType.id) {
            _selectedStatisticsPeriodType.update { newStatisticsPeriodType }
        }
    }
}
