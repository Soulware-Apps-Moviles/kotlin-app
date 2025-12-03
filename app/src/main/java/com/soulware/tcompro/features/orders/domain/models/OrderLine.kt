package com.soulware.tcompro.features.orders.domain.models

data class OrderLine(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val quantity: Int,
    val catalogProductId: Int,
    val imageUrl: String
)