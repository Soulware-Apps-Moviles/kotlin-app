package com.soulware.tcompro.features.finances.domain.models

data class Debt(
    val id: Int,
    val customerId: Int,
    val shopId: Int,
    val orderId: Int,
    val amount: Double,
    val status: String
)
