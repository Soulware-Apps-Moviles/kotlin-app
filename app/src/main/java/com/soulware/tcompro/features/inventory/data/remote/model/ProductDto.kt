package com.soulware.tcompro.features.inventory.data.remote.model

data class ProductDto(
    val id: Int?,
    val shopId: Int,
    val catalogProductId: Int,
    val name: String,
    val price: Double,
    val description: String?,
    val isAvailable: Boolean,
    val imageUrl: String?
)
