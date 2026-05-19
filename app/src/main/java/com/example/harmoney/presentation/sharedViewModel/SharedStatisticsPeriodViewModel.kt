package com.example.harmoney.presentation.sharedViewModel

import androidx.lifecycle.ViewModel
import com.example.harmoney.domain.models.StatisticsPeriod
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SharedStatisticsPeriodViewModel() : ViewModel() {
    private val _selectedStatisticsPeriod = MutableStateFlow(StatisticsPeriod.CURRENT_MONTH)
    val selectedStatisticsPeriod: StateFlow<StatisticsPeriod> =
        _selectedStatisticsPeriod.asStateFlow()

    fun statisticsPeriodChanged(newStatisticsPeriod: StatisticsPeriod) {
        if (selectedStatisticsPeriod.value.id != newStatisticsPeriod.id) {
            _selectedStatisticsPeriod.update {
                newStatisticsPeriod
            }
        }
    }
}
