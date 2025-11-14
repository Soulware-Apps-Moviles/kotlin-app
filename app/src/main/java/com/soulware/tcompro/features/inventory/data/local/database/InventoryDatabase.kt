package com.soulware.tcompro.features.inventory.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.soulware.tcompro.features.inventory.data.local.dao.ProductDao
import com.soulware.tcompro.features.inventory.data.local.model.ProductEntity

@Database(entities = [ProductEntity::class], version = 1)
abstract class InventoryDatabase: RoomDatabase() {
    abstract fun productDao(): ProductDao
}