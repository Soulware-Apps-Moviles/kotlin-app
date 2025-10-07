package com.soulware.tcompro.core

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.soulware.tcompro.R

@Composable
fun AppNav() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = Route.Shop.route) {

        composable(Route.Shop.route) {
            MainContainer(rootNavController = navController)
        }
    }
}

sealed class Route(val route: String, @param:StringRes val labelResId: Int){
    object Login : Route("login", R.string.label_login)
    object Orders : Route("orders", R.string.label_orders)
    object Inventory : Route("inventory", R.string.label_inventory)
    object Shop : Route("shop", R.string.label_shop)
    object Finances : Route("finances", R.string.label_finances)
    object Settings : Route("settings", R.string.label_settings)
}