package com.soulware.tcompro.features.orders.data.remote.services

import com.soulware.tcompro.features.orders.data.remote.models.OrderWrapperDto
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface OrderService {
    @GET("orders/v1")
    suspend fun getOrders(
        @Query("shopId") shopId: Long,
        @Query("status") status: String? = null,

    ): OrderWrapperDto

    @POST("orders/v1/{id}/accept")
    suspend fun acceptOrder(@Path("id") orderId: Int)

    @POST("orders/v1/{id}/reject")
    suspend fun rejectOrder(@Path("id") orderId: Int)

    @POST("orders/v1/{id}/advance")
    suspend fun advanceOrder(@Path("id") id: Int)

    @POST("orders/v1/{id}/cancel")
    suspend fun cancelOrder(@Path("id") id: Int)

}