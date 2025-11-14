package com.soulware.tcompro.features.inventory.data.remote.service

import com.soulware.tcompro.features.inventory.data.remote.model.ProductDto
import retrofit2.http.*

interface ProductApi {

    @GET("/catalog-products/v1/")
    suspend fun getProducts(
    ): List<ProductDto>

    @GET("/products/v1/by-shop/{shopId}")
    suspend fun getProductsByShop(
        @Path("shopId") shopId: Int
    ): List<ProductDto>

    @POST("/products/v1")
    suspend fun createProduct(
        @Body product: ProductDto
    )

    @PATCH("/products/v1")
    suspend fun updateProduct(
        @Body product: ProductDto
    )
}
