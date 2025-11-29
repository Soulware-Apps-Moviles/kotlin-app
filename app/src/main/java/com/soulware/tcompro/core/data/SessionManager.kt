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
    val shopId: Long?,
    val role: String?
)

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "tcompro_session")

@Singleton
class SessionManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object PreferencesKeys {
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val SHOP_ID = longPreferencesKey("shop_id")
        val USER_ROLE = stringPreferencesKey("user_role")
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
                shopId = preferences[PreferencesKeys.SHOP_ID],
                role = preferences[PreferencesKeys.USER_ROLE]
            )
        }

    suspend fun saveSession(accessToken: String, shopId: Long, role: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.ACCESS_TOKEN] = accessToken
            preferences[PreferencesKeys.SHOP_ID] = shopId
            preferences[PreferencesKeys.USER_ROLE] = role
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    suspend fun saveAccessToken(token: String) {
        context.dataStore.edit { it[PreferencesKeys.ACCESS_TOKEN] = token }
    }
    suspend fun saveShopId(id: Long) {
        context.dataStore.edit { it[PreferencesKeys.SHOP_ID] = id }
    }
}