package com.example.harmoney.domain.settings.theme.api.useCase

interface SetThemeUseCase {
    suspend fun execute(isThemeDark: Boolean)
}
