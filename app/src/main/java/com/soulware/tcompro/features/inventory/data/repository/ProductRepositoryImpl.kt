package com.soulware.tcompro.features.inventory.data.repository

import com.soulware.tcompro.features.inventory.data.remote.ProductApi
import com.soulware.tcompro.features.inventory.domain.model.Product
import com.soulware.tcompro.features.inventory.domain.repository.ProductRepository

class ProductRepositoryImpl(private val api: ProductApi) : ProductRepository {
    override suspend fun getProducts(): List<Product> {
        return api.getProducts()
    }

    override suspend fun addProductToInventory(product: Product) {
        api.addProductToInventory(product)
    }

    override suspend fun removeProductFromInventory(productId: String) {
        api.removeProductFromInventory(productId)
    }
}