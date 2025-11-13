package com.soulware.tcompro.features.orders.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.soulware.tcompro.features.orders.data.local.dao.OrderDao
import com.soulware.tcompro.features.orders.data.local.dao.OrderLineDao
import com.soulware.tcompro.features.orders.data.local.models.OrderEntity
import com.soulware.tcompro.features.orders.data.local.models.OrderLineEntity

@Database(entities =  [OrderEntity::class, OrderLineEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun orderDao(): OrderDao
    abstract fun orderLineDao(): OrderLineDao
}