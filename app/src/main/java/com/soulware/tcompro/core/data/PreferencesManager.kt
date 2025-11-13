/*
 * PreferencesManager (Gestor de Preferencias)
 *
 * Similar al SessionManager, pero se encarga de las preferencias locales
 * de la UI, como el tema (modo oscuro) y las notificaciones.
 * Utiliza Jetpack DataStore.
 */
package com.soulware.tcompro.core.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

private val Context.preferencesDataStore: DataStore<Preferences> by preferencesDataStore(name = "tcompro_preferences")

data class AppSettings(
    val notificationsEnabled: Boolean,
    val appTheme: String,
    val language: String
)

object AppTheme {
    const val LIGHT = "light"
    const val DARK = "dark"
    const val SYSTEM = "system"
}

@Singleton
class PreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object PreferencesKeys {
        val NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
        val APP_THEME = stringPreferencesKey("app_theme")
        val APP_LANGUAGE = stringPreferencesKey("app_language")
    }

    val appSettingsFlow: Flow<AppSettings> = context.preferencesDataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            // 5. Mapea las preferencias o usa valores por defecto
            val notifications = preferences[PreferencesKeys.NOTIFICATIONS_ENABLED] ?: true
            val theme = preferences[PreferencesKeys.APP_THEME] ?: AppTheme.SYSTEM
            val language = preferences[PreferencesKeys.APP_LANGUAGE] ?: "es" // Español por defecto

            AppSettings(notifications, theme, language)
        }


    suspend fun setNotificationsEnabled(enabled: Boolean) {
        context.preferencesDataStore.edit { preferences ->
            preferences[PreferencesKeys.NOTIFICATIONS_ENABLED] = enabled
        }
    }

    suspend fun setAppTheme(theme: String) {
        context.preferencesDataStore.edit { preferences ->
            preferences[PreferencesKeys.APP_THEME] = theme
        }
    }

    suspend fun setLanguage(language: String) {
        context.preferencesDataStore.edit { preferences ->
            preferences[PreferencesKeys.APP_LANGUAGE] = language
        }
    }
}