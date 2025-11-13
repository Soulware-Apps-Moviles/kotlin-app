/*
 * SettingsViewModel (ViewModel de Configuración)
 *
 * Es el "cerebro" de la SettingsScreen.
 *
 * Funcionalidades:
 * - Inyecta 'PreferencesManager' para leer y escribir las configuraciones locales.
 * - Inyecta 'SessionManager' para ejecutar el 'logout'.
 * - Expone el 'appSettingsFlow' para que la UI lo observe.
 * - Proporciona funciones para actualizar las preferencias y cerrar sesión.
 */
package com.soulware.tcompro.features.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soulware.tcompro.core.data.AppSettings
import com.soulware.tcompro.core.data.PreferencesManager
import com.soulware.tcompro.core.data.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager,
    private val sessionManager: SessionManager
) : ViewModel() {

    // 1. Expone el "chorro" de configuraciones como un StateFlow
    // La UI observará esto
    val settingsState: StateFlow<AppSettings> = preferencesManager.appSettingsFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AppSettings(true, "system", "es") // Valor inicial
        )

    // 2. Funciones para actualizar las preferencias
    fun setNotificationsEnabled(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.setNotificationsEnabled(enabled)
        }
    }

    fun setAppTheme(theme: String) {
        viewModelScope.launch {
            preferencesManager.setAppTheme(theme)
        }
    }

    fun setLanguage(language: String) {
        viewModelScope.launch {
            preferencesManager.setLanguage(language)
        }
    }

    // 3. Función de Logout
    fun logout() {
        viewModelScope.launch {
            sessionManager.clearSession()
        }
    }
}