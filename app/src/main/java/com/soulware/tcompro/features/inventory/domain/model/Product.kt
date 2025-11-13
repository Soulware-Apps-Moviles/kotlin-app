package com.soulware.tcompro.features.inventory.domain.model

data class Product(
    val id: Int,
    val catalogProductId: Int,
    val name: String,
    val price: Double,
    val description: String?,
    val isAvailable: Boolean,
    val imageUrl: String?
)
