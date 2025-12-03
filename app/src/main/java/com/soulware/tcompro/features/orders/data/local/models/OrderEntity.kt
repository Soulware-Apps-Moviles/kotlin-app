package com.soulware.tcompro.features.orders.data.local.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    @ColumnInfo(name = "customer_id")
    val customerId: Int,
    @ColumnInfo(name = "shop_id")
    val shopId: Int,
    @ColumnInfo(name = "pickup_method")
    val pickupMethod: String,
    @ColumnInfo(name = "payment_method")
    val paymentMethod: String,
    @ColumnInfo(name = "status")
    val status: String
)
