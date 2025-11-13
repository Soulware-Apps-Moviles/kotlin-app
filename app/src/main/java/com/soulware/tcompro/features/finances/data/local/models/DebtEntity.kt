package com.soulware.tcompro.features.finances.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "debts")
data class DebtEntity(
    @PrimaryKey val id: Int,
    val customerId: Int,
    val shopId: Int,
    val orderId: Int,
    val amount: Double,
    val status: String
)
