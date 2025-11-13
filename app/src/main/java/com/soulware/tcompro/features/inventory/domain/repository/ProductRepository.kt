package com.soulware.tcompro.features.inventory.domain.repository

import com.soulware.tcompro.features.inventory.domain.model.Product

interface ProductRepository {

    suspend fun getProducts(shopId: Int): List<Product>

    suspend fun addProductToInventory(shopId: Int, product: Product)

    suspend fun removeProductFromInventory(shopId: Int, productId: Int)
}
