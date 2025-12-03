package com.soulware.tcompro.features.orders.presentation

import androidx.annotation.StringRes
import com.soulware.tcompro.R
import com.soulware.tcompro.core.ITabRoute

sealed class OrdersScreen(
    override val route: String,
    @get:StringRes override val labelResId: Int
) : ITabRoute {
    object Incoming : OrdersScreen("incoming_orders_tab", R.string.label_incoming_orders)
    object Pending : OrdersScreen("pending_orders_tab", R.string.label_pending_orders)
}
