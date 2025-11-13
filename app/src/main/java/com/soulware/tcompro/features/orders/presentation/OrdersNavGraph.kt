package com.soulware.tcompro.features.orders.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.soulware.tcompro.features.orders.presentation.orders.incoming.IncomingOrdersScreen
import com.soulware.tcompro.features.orders.presentation.orders.pending.PendingOrdersScreen
import com.soulware.tcompro.core.TwoTabScreen

sealed class OrdersRoute(val route: String) {
    object List : OrdersRoute("orders_list")
}

@Composable
fun OrdersNavGraph(
    navController: NavHostController,
    rootNavController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = OrdersRoute.List.route
    ) {
        composable(OrdersRoute.List.route) {
            val ordersTabs = listOf(
                OrdersScreen.Incoming,
                OrdersScreen.Pending
            )

            TwoTabScreen(
                tabs = ordersTabs,
                content1 = { IncomingOrdersScreen() },
                content2 = {
                    PendingOrdersScreen(
                        onOrderClick = { order ->
                            rootNavController.navigate("orderDetail/${order.id}")
                        }
                    )
                }
            )
        }
    }
}
