package com.example.harmoney.domain.settings.theme.impl

import com.example.harmoney.domain.settings.theme.api.repository.ThemeRepository
import com.example.harmoney.domain.settings.theme.api.useCase.SetThemeUseCase

class SetThemeUseCaseImpl(
    private val themeRepository: ThemeRepository
) : SetThemeUseCase {
    override suspend fun execute(isThemeDark: Boolean) {
        return themeRepository.setTheme(isThemeDark)
    }
}
