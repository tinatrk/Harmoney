package com.example.harmoney.domain.settings.theme.api.repository

import kotlinx.coroutines.flow.Flow

interface ThemeRepository {
    fun getIsThemeDark(): Flow<Boolean>

    suspend fun setTheme(isThemeDark: Boolean)
}
