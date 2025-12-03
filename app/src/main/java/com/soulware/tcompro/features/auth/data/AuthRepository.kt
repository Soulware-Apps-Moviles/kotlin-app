package com.soulware.tcompro.features.auth.data

import com.soulware.tcompro.core.data.SessionManager
import com.soulware.tcompro.features.shop.data.ShopApiService
import javax.inject.Inject
import javax.inject.Singleton

data class AuthResult(
    val authId: String,
    val email: String,
    val accessToken: String,
    val shopId: Long,
    val role: String
)

@Singleton
class AuthRepository @Inject constructor(
    private val authApi: AuthApiService,
    private val profileApi: ProfileApiService,
    private val shopApi: ShopApiService, // Inyectamos ShopApi
    private val sessionManager: SessionManager
) {

    suspend fun login(email: String, pass: String): AuthResult? {
        return try {
            // 1. Login Supabase
            val request = AuthRequest(email, pass)
            val response = authApi.signIn(request)

            val authId = response.user?.id
            val accessToken = response.accessToken
            val userEmail = response.user?.email

            if (authId == null || accessToken == null || userEmail == null) return null

            val formattedToken = "Bearer $accessToken"
            var shopId: Long = 0
            var role: String = ""

            // 2. Intentar como DUEÑO
            try {
                // A. Obtenemos perfil de dueño
                val owner = profileApi.getOwnerByEmail(formattedToken, userEmail)
                role = "SHOP_OWNER"

                // B. Obtenemos SU tienda (Paso extra necesario)
                try {
                    val shop = shopApi.getShopByOwnerId(formattedToken, owner.id)
                    shopId = shop.id
                } catch (e: Exception) {
                    // Si falla aquí, es un dueño registrado PERO que aún no creó su tienda.
                    // Dejamos shopId en 0 para que la app le pida crearla luego.
                    shopId = 0
                }

            } catch (e: Exception) {
                // 3. Si falla, intentar como EMPLEADO
                try {
                    val shopkeeper = shopApi.getShopkeeperByEmail(formattedToken, userEmail)
                    shopId = shopkeeper.shopId ?: 0L
                    role = "SHOPKEEPER"
                } catch (e2: Exception) {
                    return null // No es nadie
                }
            }

            // 4. Guardar Sesión
            sessionManager.saveSession(accessToken, shopId, role)

            AuthResult(authId, userEmail, accessToken, shopId, role)
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
        phone: String,
        role: String
    ): Boolean {
        return try {
            val supabaseRequest = AuthRequest(email, pass)
            val supabaseResponse = authApi.signUp(supabaseRequest)
            val authId = supabaseResponse.user?.id ?: return false
            val accessToken = supabaseResponse.accessToken ?: return false

            // Limpieza de teléfono
            val digitsOnly = phone.filter { it.isDigit() }
            val finalPhone = if (digitsOnly.length <= 9) "+51$digitsOnly" else "+$digitsOnly"

            val formattedToken = "Bearer $accessToken"

            val profileRequest = CreateProfileRequest(
                firstName = firstName,
                lastName = lastName,
                email = email,
                phone = finalPhone,
                authId = authId,
                role = role
            )

            profileApi.createProfile(formattedToken, profileRequest)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}