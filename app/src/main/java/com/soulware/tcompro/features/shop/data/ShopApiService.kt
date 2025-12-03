package com.soulware.tcompro.features.shop.data

import com.google.gson.annotations.SerializedName
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ShopApiService {

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

    @GET("api/v1/shops/{shopId}/shopkeepers")
    suspend fun getShopkeepers(@Path("shopId") shopId: String): List<ShopkeeperResponse>

    @POST("api/v1/shops/{shopId}/shopkeepers")
    suspend fun hireShopkeeper(@Path("shopId") shopId: String, @Body request: HireShopkeeperRequest): ShopkeeperResponse

    @DELETE("api/v1/shops/{shopId}/shopkeepers/{shopkeeperId}")
    suspend fun fireShopkeeper(@Path("shopId") shopId: String, @Path("shopkeeperId") shopkeeperId: Long): Unit
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

data class ShopkeeperResource(val id: Long, val profileId: String, val shopId: Long?)
data class ShopkeeperResponse(val id: Long, val shopId: Long, val authId: String, val firstName: String, val lastName: String, val email: String, val phone: String, val isHired: Boolean)
data class HireShopkeeperRequest(val authId: String)