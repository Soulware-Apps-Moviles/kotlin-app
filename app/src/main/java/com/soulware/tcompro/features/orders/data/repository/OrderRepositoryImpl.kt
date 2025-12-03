    package com.soulware.tcompro.features.orders.data.repository

    import com.soulware.tcompro.core.data.SessionManager
    import com.soulware.tcompro.features.orders.data.local.dao.OrderDao
    import com.soulware.tcompro.features.orders.data.local.dao.OrderLineDao
    import com.soulware.tcompro.features.orders.data.local.models.OrderEntity
    import com.soulware.tcompro.features.orders.data.local.models.OrderLineEntity
    import com.soulware.tcompro.features.orders.data.remote.models.OrderDto
    import com.soulware.tcompro.features.orders.data.remote.models.OrderLineDto
    import com.soulware.tcompro.features.orders.data.remote.services.OrderService
    import com.soulware.tcompro.features.orders.domain.models.Order
    import com.soulware.tcompro.features.orders.domain.models.OrderLine
    import com.soulware.tcompro.features.orders.domain.repository.OrderRepository
    import kotlinx.coroutines.Dispatchers
    import kotlinx.coroutines.flow.first
    import kotlinx.coroutines.runBlocking
    import kotlinx.coroutines.withContext
    import javax.inject.Inject

    class OrdersRepositoryImpl @Inject constructor(
        private val service: OrderService,
        private val orderDao: OrderDao,
        private val orderlineDao: OrderLineDao,
        private val sessionManager: SessionManager
    ) : OrderRepository {

        private fun getShopIdFromSession(): Long {
            val shopId = runBlocking {
                sessionManager.userSessionFlow.first().shopId
            }
            return shopId ?: throw IllegalStateException("El usuario no tiene un Shop ID asignado para esta operación.")
        }

        override suspend fun getIncomingOrders(): List<Order> = withContext(Dispatchers.IO) {
            val shopId = getShopIdFromSession()
            try {
                val response = service.getOrders(shopId, status = "PLACED")
                val orders = response.map { it.toDomain() }

                orderDao.clearAllOrders()
                orders.forEach { order ->
                    orderDao.insertOrders(order.toEntity())
                    orderlineDao.insertOrderLines(*order.orderLines.map { it.toEntity(order.id) }.toTypedArray())
                }

                orders
            } catch (e: Exception) {
                val localOrders = orderDao.getAllOrders()
                localOrders.map { orderEntity ->
                    val lines = orderlineDao.getOrderLinesForOrder(orderEntity.id)
                    orderEntity.toDomain(lines)
                }
            }
        }

        override suspend fun getPendingOrders(): List<Order> = withContext(Dispatchers.IO) {
            val shopId = getShopIdFromSession()

            try {
                val response = service.getOrders(shopId = shopId, status = "ACCEPTED")
                val orders = response.map { it.toDomain() }

                orderDao.clearAllOrders()
                orders.forEach { order ->
                    orderDao.insertOrders(order.toEntity())
                    orderlineDao.insertOrderLines(*order.orderLines.map { it.toEntity(order.id) }.toTypedArray())
                }

                orders
            } catch (e: Exception) {
                val localOrders = orderDao.getAllOrders()
                localOrders.map { orderEntity ->
                    val lines = orderlineDao.getOrderLinesForOrder(orderEntity.id)
                    orderEntity.toDomain(lines)
                }
            }
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
            val shopId = getShopIdFromSession()
            try {
                service.getOrders(shopId = shopId).map { it.toDomain() }.find { it.id == orderId }

            } catch (e: Exception) {
                val orderEntity = orderDao.getAllOrders().find { it.id == orderId } ?: return@withContext null
                val lines = orderlineDao.getOrderLinesForOrder(orderId)
                orderEntity.toDomain(lines)
            }
        }
    }
    private fun OrderDto.toDomain(): Order = Order(
        id = id,
        customerId = customerId,
        shopId = shopId,
        paymentMethod = paymentMethod,
        pickupMethod = pickupMethod,
        status = status,
        orderLines = orderLines.map { it.toDomain() }
    )

    private fun OrderLineDto.toDomain(): OrderLine = OrderLine(
        id = id,
        catalogProductId = catalogProductId,
        description = description,
        imageUrl = imageUrl,
        name = name,
        price = price,
        quantity = quantity
    )

    private fun Order.toEntity(): OrderEntity = OrderEntity(
        id = id,
        customerId = customerId,
        shopId = shopId,
        paymentMethod = paymentMethod,
        pickupMethod = pickupMethod,
        status = status
    )

    private fun OrderLine.toEntity(orderId: Int): OrderLineEntity = OrderLineEntity(
        id = id,
        orderId = orderId,
        catalogProductId = catalogProductId,
        description = description,
        imageUrl = imageUrl,
        name = name,
        price = price,
        quantity = quantity
    )

    private fun OrderEntity.toDomain(lines: List<OrderLineEntity>): Order = Order(
        id = id,
        customerId = customerId,
        shopId = shopId,
        paymentMethod = paymentMethod,
        pickupMethod = pickupMethod,
        status = status,
        orderLines = lines.map { it.toDomain() }
    )

    private fun OrderLineEntity.toDomain(): OrderLine = OrderLine(
        id = id,
        catalogProductId = catalogProductId,
        description = description,
        imageUrl = imageUrl,
        name = name,
        price = price,
        quantity = quantity
    )