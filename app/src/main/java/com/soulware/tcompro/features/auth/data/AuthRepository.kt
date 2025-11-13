/*
 * AuthRepository (Repositorio de Autenticación)
 *
 * Esta clase es el "traductor" o Repositorio para la lógica de autenticación.
 * Es responsable de coordinar las llamadas a las dos APIs (Supabase y el backend Tcompro).
 *
 * Funcionalidades:
 * - Inyecta 'AuthApiService' (para Supabase) y 'ProfileApiService' (para Tcompro).
 * - Inyecta el 'SessionManager' para guardar la sesión.
 * - Implementa el flujo de Login en 2 pasos:
 * 1. Autentica en Supabase (obtiene token).
 * 2. Guarda el token (para que el AuthInterceptor lo use).
 * 3. Llama al backend Tcompro (obtiene shopId).
 * 4. Guarda el shopId.
 * - Implementa el flujo de Register en 2 pasos:
 * 1. Crea el usuario en Supabase (obtiene authId).
 * 2. Crea el perfil en el backend Tcompro (envía el authId).
 */
package com.soulware.tcompro.features.auth.data

import com.soulware.tcompro.core.data.SessionManager
import javax.inject.Inject
import javax.inject.Singleton

data class AuthResult(
    val authId: String,
    val email: String,
    val accessToken: String,
    val shopId: Long
)

@Singleton
class AuthRepository @Inject constructor(
    private val authApi: AuthApiService,
    private val profileApi: ProfileApiService,
    private val sessionManager: SessionManager
) {

    suspend fun login(email: String, pass: String): AuthResult? {
        return try {
            val request = AuthRequest(email, pass)
            val response = authApi.signIn(request)

            val authId = response.user?.id
            val accessToken = response.accessToken
            val userEmail = response.user?.email

            if (authId == null || accessToken == null || userEmail == null) {
                return null
            }


            val formattedToken = "Bearer $accessToken"

            val owner = profileApi.getOwnerByEmail(
                token = formattedToken,
                email = userEmail
            )
            val shopId = owner.shopId

            sessionManager.saveAccessToken(accessToken)
            sessionManager.saveShopId(shopId)


            AuthResult(
                authId = authId,
                email = userEmail,
                accessToken = accessToken,
                shopId = shopId
            )
        } catch (e: Exception) {
            e.printStackTrace()
            sessionManager.clearSession()
            null
        }
    }

    suspend fun register(
        email: String,
        pass: String,
        firstName: String,
        lastName: String,
        phone: String
    ): Boolean {
        return try {
            val supabaseRequest = AuthRequest(email, pass)
            val supabaseResponse = authApi.signUp(supabaseRequest)

            val authId = supabaseResponse.user?.id
            if (authId == null) {
                return false
            }

            val profileRequest = CreateProfileRequest(
                firstName = firstName,
                lastName = lastName,
                email = email,
                phone = phone,
                authId = authId
            )
            profileApi.createProfile(profileRequest)

            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }
}