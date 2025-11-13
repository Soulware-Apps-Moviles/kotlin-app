package com.soulware.tcompro.features.inventory.domain.repository

import com.soulware.tcompro.features.inventory.domain.model.Product

interface ProductRepository {
    suspend fun getProducts(): List<Product>
    suspend fun addProductToInventory(product: Product)
    suspend fun removeProductFromInventory(productId: String)
}