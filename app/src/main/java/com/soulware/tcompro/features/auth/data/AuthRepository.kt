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
            // 1. Autenticar con Supabase
            val request = AuthRequest(email, pass)
            val response = authApi.signIn(request)

            val authId = response.user?.id
            val accessToken = response.accessToken
            val userEmail = response.user?.email

            if (authId == null || accessToken == null || userEmail == null) {
                return null
            }

            // 2. Obtener datos del Dueño (Shop ID) desde tu Backend
            // Pasamos el token manualmente usando "Bearer "
            val formattedToken = "Bearer $accessToken"

            val owner = profileApi.getOwnerByEmail(
                token = formattedToken,
                email = userEmail
            )

            val shopId = owner.shopId

            // 3. Guardar la sesión real
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
            // 1. Crear usuario en Supabase
            val supabaseRequest = AuthRequest(email, pass)
            val supabaseResponse = authApi.signUp(supabaseRequest)

            val authId = supabaseResponse.user?.id
            // OBTENEMOS EL TOKEN DEL REGISTRO
            val accessToken = supabaseResponse.accessToken

            // Verificamos que tengamos ID y Token
            // Nota: Si Supabase pide confirmar email, el token podría ser null.
            // Para la demo, asume que 'Enable Email Confirmation' está OFF en Supabase.
            if (authId == null || accessToken == null) {
                return false
            }

            // 2. Crear perfil en tu Backend (ENVIANDO EL TOKEN)
            val formattedToken = "Bearer $accessToken" // <--- Token formateado

            // 1. Limpiamos el teléfono de espacios o guiones
            val cleanPhoneInput = phone.replace("\\s".toRegex(), "").replace("-", "")

            // 2. Agregamos el prefijo si falta
            val finalPhone = if (cleanPhoneInput.startsWith("+")) cleanPhoneInput else "+51$cleanPhoneInput"

            val profileRequest = CreateProfileRequest(
                firstName = firstName,
                lastName = lastName,
                email = email,
                phone = finalPhone,
                authId = authId
            )

            // Pasamos el token aquí
            profileApi.createProfile(
                token = formattedToken,
                request = profileRequest
            )

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}