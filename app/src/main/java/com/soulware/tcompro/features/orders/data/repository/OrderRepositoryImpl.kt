package com.soulware.tcompro.features.orders.data.repository

import com.soulware.tcompro.features.orders.data.remote.services.OrderService
import com.soulware.tcompro.features.orders.domain.models.Order
import com.soulware.tcompro.features.orders.domain.repository.OrderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OrdersRepositoryImpl @Inject constructor(
    private val service: OrderService
) : OrderRepository {

    override suspend fun getIncomingOrders(): List<Order> = withContext(Dispatchers.IO) {
        service.getOrders(status = "PENDING")
    }

    override suspend fun getPendingOrders(): List<Order> = withContext(Dispatchers.IO) {
        service.getOrders(status = "ACCEPTED")
    }

    override suspend fun acceptOrder(orderId: Int) = withContext(Dispatchers.IO) {
        service.acceptOrder(orderId)
    }

    override suspend fun rejectOrder(orderId: Int) = withContext(Dispatchers.IO) {
        service.rejectOrder(orderId)
    }

    override suspend fun advanceOrder(orderId: Int) = withContext(Dispatchers.IO) {
        service.advanceOrder(orderId)
    }

    override suspend fun cancelOrder(orderId: Int) = withContext(Dispatchers.IO) {
        service.cancelOrder(orderId)
    }

    override suspend fun getOrderById(orderId: Int): Order? = withContext(Dispatchers.IO) {
        service.getOrders().find { it.id == orderId }
    }
}
