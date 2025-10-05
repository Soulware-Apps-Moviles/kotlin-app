package com.soulware.tcompro.core

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.soulware.tcompro.core.ui.theme.TcomproTheme
import com.soulware.tcompro.features.orders.presentation.Order
import com.soulware.tcompro.features.shop.presentation.Shop
import com.soulware.tcompro.features.shop.presentation.ShopViewModel

@Composable
fun AppNav(){
    val navController = rememberNavController()
    val viewModel: ShopViewModel = hiltViewModel()
    NavHost(navController, startDestination = Route.Shop.route){

        composable(Route.Shop.route)
        {
            Shop(viewModel) {
                navController.navigate(Route.Orders.route)
            }
        }
        composable(Route.Orders.route)
        {
            Order {  }
        }
    }

    Main {  }
}

@Preview
@Composable
fun AppNavPreview(){
    TcomproTheme {
        AppNav()
    }
}

sealed class Route(val route: String){
    object Login : Route("login")
    object Orders : Route("orders")
    object Inventory : Route("inventory")
    object Shop : Route("shop")
    object Finances : Route("finances")
    object Settings : Route("settings")
}