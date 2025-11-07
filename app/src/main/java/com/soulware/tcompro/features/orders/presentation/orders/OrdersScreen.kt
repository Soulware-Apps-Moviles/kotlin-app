package com.soulware.tcompro.features.orders.presentation.orders

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.soulware.tcompro.R
import com.soulware.tcompro.core.ITabRoute
import com.soulware.tcompro.core.TwoTabScreen
import com.soulware.tcompro.features.orders.domain.models.Order
import com.soulware.tcompro.features.orders.presentation.detail.OrderDetailScreen
import com.soulware.tcompro.features.orders.presentation.orders.incoming.IncomingOrdersScreen
import com.soulware.tcompro.features.orders.presentation.orders.pending.PendingOrdersScreen

sealed class OrdersInnerTabRoute(
    override val route: String,
    @get:StringRes override val labelResId: Int
) : ITabRoute {
    object Incoming : OrdersInnerTabRoute("incoming_orders_tab", R.string.label_incoming_orders)
    object Pending : OrdersInnerTabRoute("pending_orders_tab", R.string.label_pending_orders)
}
@Composable
fun OrdersScreen() {
    val ordersTabs = listOf(
        OrdersInnerTabRoute.Incoming,
        OrdersInnerTabRoute.Pending
    )

    var selectedOrder: Order? by remember { mutableStateOf<Order?>(null) }

    if (selectedOrder == null) {
        TwoTabScreen(
            tabs = ordersTabs,
            content1 = { IncomingOrdersScreen() },
            content2 = {
                PendingOrdersScreen(
                    onOrderClick = { order -> selectedOrder = order }
                )
            }
        )
    } else {
        OrderDetailScreen(
            orderId = selectedOrder!!.id,
            onBackClick = { selectedOrder = null }
        )
    }
}