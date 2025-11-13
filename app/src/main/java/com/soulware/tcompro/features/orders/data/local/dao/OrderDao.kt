package com.soulware.tcompro.features.orders.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.soulware.tcompro.features.orders.data.local.models.OrderEntity

@Dao
interface OrderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrders(vararg entity: OrderEntity)

    @Query("DELETE FROM orders WHERE id = :id")
    suspend fun deleteOrder(id: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM orders WHERE id = :id)")
    suspend fun existsOrder(id: Int): Boolean

    @Query("SELECT * FROM orders")
    suspend fun getAllOrders(): List<OrderEntity>

    @Query("DELETE FROM orders")
    suspend fun clearAllOrders()
}
