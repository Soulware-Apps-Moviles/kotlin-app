package com.soulware.tcompro.features.inventory.domain.repository

import com.soulware.tcompro.features.inventory.domain.model.Product

interface ProductRepository {

    suspend fun getCatalogProducts(): List<Product>

    suspend fun getInventoryProducts(shopId: Int): List<Product>

    suspend fun addProductToInventory(shopId: Int, product: Product)

    suspend fun removeProductFromInventory(shopId: Int, productId: Int)

    suspend fun updateProductPrice(shopId: Int, productId: Int, newPrice: Double)
}
