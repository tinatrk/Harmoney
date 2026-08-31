package com.example.harmoney.domain.settings.theme.impl

import com.example.harmoney.domain.settings.theme.api.repository.ThemeRepository
import com.example.harmoney.domain.settings.theme.api.useCase.GetIsThemeDarkUseCase
import kotlinx.coroutines.flow.Flow

class GetIsThemeDarkUseCaseImpl(
    private val themeRepository: ThemeRepository
) : GetIsThemeDarkUseCase {
    override fun execute(): Flow<Boolean> {
        return themeRepository.getIsThemeDark()
    }
}
