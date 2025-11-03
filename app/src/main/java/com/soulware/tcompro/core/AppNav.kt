package com.soulware.tcompro.core

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.soulware.tcompro.R
import com.soulware.tcompro.features.shop.presentation.AddEmployeeByCodeScreen
import com.soulware.tcompro.features.shop.presentation.AddEmployeeByQRScreen
import androidx.navigation.NavType
import androidx.navigation.navArgument
@Composable
fun AppNav() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = RootRoute.Main.route) {

        composable(RootRoute.Main.route) {
            MainContainer(R.drawable.app_logo, navController = navController)
        }
        composable("add_employee_qr") {
            AddEmployeeByQRScreen(navController)
        }

        composable(
            route = "add_employee_code?scannedCode={scannedCode}",
            arguments = listOf(
                navArgument("scannedCode") {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) {
            AddEmployeeByCodeScreen(navController)
        }
    }
}

sealed class RootRoute(val route: String) {
    object Login : RootRoute("login")
    object Main : RootRoute("main_container")
}