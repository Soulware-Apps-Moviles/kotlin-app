package com.soulware.tcompro.features.finances.domain.models

data class Payment(
    val id: Int,
    val customerId: Int,
    val orderId: Int,
    val amount: Double,
    val paymentMethodId: Int,
    val shopId: Int
)