package com.soulware.tcompro.features.inventory.data.remote.model

import com.soulware.tcompro.features.inventory.domain.model.ProductCategory

data class ProductDto(
    val id: String,
    val name: String,
    val price: Double,
    val category: ProductCategory,
    val stock: Int
)