package com.soulware.tcompro.features.orders.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.soulware.tcompro.features.orders.data.local.models.OrderLineEntity

@Dao
interface OrderLineDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrderLines(vararg entity: OrderLineEntity)

    @Query("SELECT * FROM order_lines WHERE order_id = :orderId")
    suspend fun getOrderLinesForOrder(orderId: Int): List<OrderLineEntity>

    @Query("DELETE FROM order_lines WHERE order_id = :orderId")
    suspend fun deleteOrderLines(orderId: Int)
}
