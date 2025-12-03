package com.soulware.tcompro.features.shop.data

import com.google.gson.annotations.SerializedName
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ShopApiService {
    @GET("trusted-customers/v1/by-shop/{shopId}")
    suspend fun getTrustedCustomers(
        @Header("Authorization") token: String,
        @Path("shopId") shopId: Long
    ): List<TrustedCustomerResource>

    // 2. Agregar cliente confiable (usamos POST porque existe CreateTrustedCustomerResource)
    @POST("trusted-customers/v1/")
    suspend fun addTrustedCustomer(
        @Header("Authorization") token: String,
        @Body request: CreateTrustedCustomerRequest
    ): TrustedCustomerResource

    // 3. Eliminar cliente confiable (asumiendo DELETE estándar)
    @DELETE("trusted-customers/v1/{id}")
    suspend fun deleteTrustedCustomer(
        @Header("Authorization") token: String,
        @Path("id") id: Long
    )
    @PATCH("shopkeepers/v1/hire")
    suspend fun hireShopkeeper(
        @Header("Authorization") token: String,
        @Body request: HireShopkeeperRequest
    ): ShopkeeperResource

    @POST("shops/v1/")
    suspend fun createShop(
        @Header("Authorization") token: String,
        @Body request: CreateShopRequest
    ): ShopResource

    @GET("shops/v1/by-owner/{ownerId}")
    suspend fun getShopByOwnerId(
        @Header("Authorization") token: String,
        @Path("ownerId") ownerId: Long
    ): ShopResource

    @GET("shopkeepers/v1/")
    suspend fun getShopkeeperByEmail(
        @Header("Authorization") token: String,
        @Query("email") email: String
    ): ShopkeeperResource

    @GET("shopkeepers/v1/by-shop/{shopId}")
    suspend fun getShopkeepers(
        @Header("Authorization") token: String,
        @Path("shopId") shopId: Long
    ): List<ShopkeeperResource>

    @PATCH("shopkeepers/v1/fire/{id}")
    suspend fun fireShopkeeper(
        @Header("Authorization") token: String,
        @Path("id") shopkeeperId: Long
    ): ShopkeeperResource
}

data class CreateShopRequest(
    @SerializedName("OwnerId") val ownerId: Long,

    val paymentMethods: List<String>,
    val pickupMethods: List<String>,
    val maxCreditPerCustomer: Double,

    val name: String,
    val latitude: Double,
    val longitude: Double
)

data class ShopResource(
    val id: Long,
    val name: String?,
    val ownerId: Long
)

data class ShopkeeperResource(
    val id: Long,
    val authId: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String?,
    val shopId: Long?,
    val isHired: Boolean?
)

data class ShopkeeperResponse(val id: Long, val shopId: Long, val authId: String, val firstName: String, val lastName: String, val email: String, val phone: String, val isHired: Boolean)
data class HireShopkeeperRequest(
    val shopId: Long,
    val email: String
)

data class CreateTrustedCustomerRequest(
    val shopId: Long,
    val email: String
)

// Respuesta del backend
data class TrustedCustomerResource(
    val id: Long,
    val shopId: Long,
    val firstName: String,
    val lastName: String,
    val email: String,
    val photoUrl: String? // Campo opcional por si el backend lo manda
)