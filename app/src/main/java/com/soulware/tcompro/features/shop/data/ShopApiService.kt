package com.soulware.tcompro.features.shop.data

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.DELETE
interface ShopApiService {

    @GET("api/v1/shops/{shopId}/shopkeepers")
    suspend fun getShopkeepers(
        @Path("shopId") shopId: String
    ): List<ShopkeeperResponse>

    @POST("api/v1/shops/{shopId}/shopkeepers")
    suspend fun hireShopkeeper(
        @Path("shopId") shopId: String,
        @Body request: HireShopkeeperRequest
    ): ShopkeeperResponse

    @DELETE("api/v1/shops/{shopId}/shopkeepers/{shopkeeperId}")
    suspend fun fireShopkeeper(
        @Path("shopId") shopId: String,
        @Path("shopkeeperId") shopkeeperId: Long
    ): Unit
}

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


data class HireShopkeeperRequest(
    val authId: String
)