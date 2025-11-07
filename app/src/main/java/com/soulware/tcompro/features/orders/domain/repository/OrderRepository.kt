package com.soulware.tcompro.features.orders.domain.repository

import com.soulware.tcompro.features.orders.domain.models.Order

interface OrderRepository {
    suspend fun getIncomingOrders(): List<Order>
    suspend fun acceptOrder(orderId: Int)
    suspend fun rejectOrder(orderId: Int)

    suspend fun getPendingOrders(): List<Order>
    suspend fun advanceOrder(orderId: Int)
    suspend fun cancelOrder(orderId: Int)

    suspend fun getOrderById(orderId: Int): Order?
}