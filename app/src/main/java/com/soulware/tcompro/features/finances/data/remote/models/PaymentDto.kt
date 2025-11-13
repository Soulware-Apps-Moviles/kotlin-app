package com.soulware.tcompro.features.finances.data.remote.models

data class PaymentDto(
    val id: Int,
    val customerId: Int,
    val orderId: Int,
    val amount: Double,
    val paymentMethodId: Int,
    val shopId: Int
)

