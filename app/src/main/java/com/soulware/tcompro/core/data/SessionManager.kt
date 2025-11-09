/*
 * SessionManager (Gestor de Sesión)
 *
 * Esta clase se encarga de gestionar la sesión del usuario utilizando Jetpack DataStore.
 * Define el "llavero" de la app ("tcompro_session") para persistir datos.
 *
 * Funcionalidades:
 * - Define la estructura de datos UserSession (accessToken, shopId).
 * - Expone un 'userSessionFlow' para observar cambios en la sesión en tiempo real.
 * - Proporciona funciones para guardar el 'accessToken' y el 'shopId' por separado.
 * - Proporciona una función 'clearSession' para cerrar la sesión (logout).
 */
package com.soulware.tcompro.core.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

data class UserSession(
    val accessToken: String?,
    val shopId: Long?
)

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "tcompro_session")

@Singleton
class SessionManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object PreferencesKeys {
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val SHOP_ID = longPreferencesKey("shop_id")
    }

    val userSessionFlow: Flow<UserSession> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            UserSession(
                accessToken = preferences[PreferencesKeys.ACCESS_TOKEN],
                shopId = preferences[PreferencesKeys.SHOP_ID]
            )
        }

    suspend fun saveAccessToken(accessToken: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.ACCESS_TOKEN] = accessToken
        }
    }

    suspend fun saveShopId(shopId: Long) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.SHOP_ID] = shopId
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}