package com.soulware.tcompro.features.inventory.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val price: Double,
    val category: String,
    val stock: Int,
    val imageUrl: String? = null,
    val shopId: Int
)
