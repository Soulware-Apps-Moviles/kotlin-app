package com.soulware.tcompro.features.shop.data

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ShopApiService {

    // --- NUEVO: Obtener la tienda usando el ID del Dueño ---
    @GET("shops/v1/by-owner/{ownerId}")
    suspend fun getShopByOwnerId(
        @Header("Authorization") token: String, // Requiere token
        @Path("ownerId") ownerId: Long
    ): ShopResource

    // --- CORREGIDO: Ruta sin "email" al final ---
    @GET("shopkeepers/v1/")
    suspend fun getShopkeeperByEmail(
        @Header("Authorization") token: String,
        @Query("email") email: String
    ): ShopkeeperResource

    // ... (Tus otros endpoints hire, fire, getShopkeepers siguen igual) ...
    @GET("api/v1/shops/{shopId}/shopkeepers")
    suspend fun getShopkeepers(@Path("shopId") shopId: String): List<ShopkeeperResponse>

    @POST("api/v1/shops/{shopId}/shopkeepers")
    suspend fun hireShopkeeper(@Path("shopId") shopId: String, @Body request: HireShopkeeperRequest): ShopkeeperResponse

    @DELETE("api/v1/shops/{shopId}/shopkeepers/{shopkeeperId}")
    suspend fun fireShopkeeper(@Path("shopId") shopId: String, @Path("shopkeeperId") shopkeeperId: Long): Unit
}

// --- Data Classes ---

data class ShopResource(
    val id: Long,
    val name: String,
    val ruc: String?,
    val address: String?,
    val logoUrl: String?,
    val ownerId: Long
)

data class ShopkeeperResource(
    val id: Long,
    val profileId: String,
    val shopId: Long?
)

data class ShopkeeperResponse(
    val id: Long,
    val shopId: Long,
    val authId: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String,
    val isHired: Boolean
)

data class HireShopkeeperRequest(val authId: String)