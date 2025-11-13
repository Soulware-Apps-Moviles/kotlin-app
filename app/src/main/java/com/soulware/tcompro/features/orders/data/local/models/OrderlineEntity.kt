package com.soulware.tcompro.features.orders.data.local.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "order_lines")
data class OrderLineEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "order_id")
    val orderId: Int,

    @ColumnInfo(name = "catalog_product_id")
    val catalogProductId: Int,

    val name: String,
    val description: String,
    val quantity: Int,
    val price: Double,

    @ColumnInfo(name = "image_url")
    val imageUrl: String
)

