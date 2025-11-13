package com.soulware.tcompro.features.finances.data.remote.models

data class DebtDto(
    val id: Int,
    val customerId: Int,
    val shopId: Int,
    val orderId: Int,
    val amount: Double,
    val status: String
)

