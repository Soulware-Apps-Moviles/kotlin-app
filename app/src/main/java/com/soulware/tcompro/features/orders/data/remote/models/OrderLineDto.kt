package com.soulware.tcompro.features.orders.data.remote.models

data class OrderLineDto(
    val catalogProductId: Int,
    val description: String,
    val id: Int,
    val imageUrl: String,
    val name: String,
    val price: Double,
    val quantity: Int
)