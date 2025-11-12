package com.soulware.tcompro.core

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.soulware.tcompro.R
import com.soulware.tcompro.features.orders.presentation.detail.OrderDetailScreen

@Composable
fun AppNav() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = RootRoute.Main.route
    ) {
        composable(RootRoute.Main.route) {
            MainContainer(
                logoImageResId = R.drawable.app_logo,
                rootNavController = navController
            )
        }

        composable("orderDetail/{id}") { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("id")?.toInt() ?: 0
            OrderDetailScreen(
                orderId = orderId,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}

sealed class RootRoute(val route: String) {
    object Login : RootRoute("login")
    object Main : RootRoute("main_container")
}