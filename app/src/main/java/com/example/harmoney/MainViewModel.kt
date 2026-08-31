package com.example.harmoney

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.harmoney.domain.settings.theme.api.useCase.GetIsThemeDarkUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class MainViewModel(getIsThemeDarkUseCase: GetIsThemeDarkUseCase) : ViewModel() {
    val isThemeDark = getIsThemeDarkUseCase.execute()
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            null
        )
}
