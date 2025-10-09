package com.soulware.tcompro.features.orders.presentation

import androidx.annotation.StringRes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.soulware.tcompro.R
import com.soulware.tcompro.core.ITabRoute
import com.soulware.tcompro.core.TwoTabScreen

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
    TwoTabScreen(
        tabs = ordersTabs,
        content1 = { Text(stringResource(R.string.placeholder_incoming_orders)) },
        content2 = { Text(stringResource(R.string.placeholder_pending_orders)) }
    )
}