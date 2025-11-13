package com.soulware.tcompro.features.finances.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "payments")
data class PaymentEntity(
    @PrimaryKey val id: Int,
    val customerId: Int,
    val orderId: Int,
    val amount: Double,
    val paymentMethodId: Int,
    val shopId: Int,
    val timestamp: Long
)
