package com.soulware.tcompro.features.inventory.data.repository

import com.soulware.tcompro.features.inventory.data.remote.model.ProductDto
import com.soulware.tcompro.features.inventory.data.remote.service.ProductApi
import com.soulware.tcompro.features.inventory.domain.model.Product
import com.soulware.tcompro.features.inventory.domain.repository.ProductRepository

class ProductRepositoryImpl(
    private val api: ProductApi
) : ProductRepository {

    override suspend fun getCatalogProducts(): List<Product> {
        return api.getProducts().map { it.toDomain() }
    }

    override suspend fun getInventoryProducts(shopId: Int): List<Product> {
        return api.getProductsByShop(shopId).map { it.toDomain() }
    }

    override suspend fun addProductToInventory(shopId: Int, product: Product) {
        api.createProduct(product.toDto(shopId))
    }

    override suspend fun removeProductFromInventory(shopId: Int, productId: Int) {
        val dto = ProductDto(
            id = productId,
            shopId = shopId,
            catalogProductId = 0, // Not used but required
            name = "",
            price = 0.0,
            description = null,
            isAvailable = false,
            imageUrl = null
        )
        api.updateProduct(dto)
    }

    override suspend fun updateProductPrice(shopId: Int, productId: Int, newPrice: Double) {
        val dto = ProductDto(
            id = productId,
            shopId = shopId,
            price = newPrice,
            catalogProductId = 0, // Not used but required
            name = "",
            description = null,
            isAvailable = false,
            imageUrl = null
        )
        api.updateProduct(dto)
    }
}

// =====================
// MAPPERS
// =====================

fun ProductDto.toDomain(): Product {
    return Product(
        id = id ?: 0,
        catalogProductId = catalogProductId,
        name = name,
        price = price,
        description = description,
        isAvailable = isAvailable,
        imageUrl = imageUrl
    )
}

fun Product.toDto(shopId: Int): ProductDto {
    return ProductDto(
        id = id,
        shopId = shopId,
        catalogProductId = catalogProductId,
        name = name,
        price = price,
        description = description,
        isAvailable = isAvailable,
        imageUrl = imageUrl
    )
}
