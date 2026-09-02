package com.example.harmoney.data.settings.theme.impl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.example.harmoney.domain.settings.theme.api.repository.ThemeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ThemeRepositoryImpl(private val dataStore: DataStore<Preferences>) : ThemeRepository {
    override fun getIsThemeDark(): Flow<Boolean> =
        dataStore.data.map { prefs ->
            prefs[DARK_THEME_KEY] ?: DEFAULT_THEME
        }

    override suspend fun setTheme(isThemeDark: Boolean) {
        dataStore.edit { prefs ->
            prefs[DARK_THEME_KEY] = isThemeDark
        }
    }

    companion object {
        private val DARK_THEME_KEY = booleanPreferencesKey("dark_theme")
        private const val DEFAULT_THEME = false
    }
}
