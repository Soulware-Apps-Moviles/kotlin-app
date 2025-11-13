package com.soulware.tcompro.features.inventory.data.repository


import com.soulware.tcompro.features.inventory.data.remote.model.ProductDto
import com.soulware.tcompro.features.inventory.data.remote.service.ProductApi

import com.soulware.tcompro.features.inventory.domain.model.Product
import com.soulware.tcompro.features.inventory.domain.repository.ProductRepository

class ProductRepositoryImpl(private val api: ProductApi) : ProductRepository {
    override suspend fun getProducts(): List<Product> {
        return api.getProducts()
    }

    override suspend fun addProductToInventory(product: Product) {
        val productDto = ProductDto(
            id = product.id,
            name = product.name,
            price = product.price,
            category = product.category,
            stock = product.stock,
            imageUrl = product.imageUrl
        )
        api.addProductToInventory(productDto)
    }

    override suspend fun removeProductFromInventory(productId: String) {
        api.removeProductFromInventory(productId)
    }
}