package com.soulware.tcompro.features.inventory.data.remote

import com.soulware.tcompro.features.inventory.domain.model.Product
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ProductApi {

    @GET("inventory")
    suspend fun getProducts(): List<Product>

    @POST("inventory")
    suspend fun addProductToInventory(@Body product: Product)

    @DELETE("inventory/{productId}")
    suspend fun removeProductFromInventory(@Path("productId") productId: String)
}