package com.soulware.tcompro.features.inventory.data.remote.model

data class ProductsWrapperDto(
    val limit: Int?,
    val products: List<ProductDto>?,
    val skip: Int?,
    val total: Int?
)
