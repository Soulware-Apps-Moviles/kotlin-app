package com.soulware.tcompro.core

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.soulware.tcompro.R
import com.soulware.tcompro.features.auth.presentation.LoginScreen
import com.soulware.tcompro.features.auth.presentation.RegisterScreen
import com.soulware.tcompro.features.orders.presentation.detail.OrderDetailScreen
import com.soulware.tcompro.features.settings.presentation.AboutScreen
import com.soulware.tcompro.features.shop.presentation.AddEmployeeByCodeScreen
import com.soulware.tcompro.features.shop.presentation.AddEmployeeByQRScreen
import com.soulware.tcompro.features.shop.presentation.AddTrustedCustomerScreen

@Composable
fun AppNav() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = RootRoute.Main.route
    ) {

        // LOGIN
        composable(RootRoute.Login.route) {
            LoginScreen(navController = navController)
        }

        // REGISTER
        composable(RootRoute.Register.route) {
            RegisterScreen(navController = navController)
        }

        // MAIN CONTAINER
        composable(RootRoute.Main.route) {
            MainContainer(
                logoImageResId = R.drawable.app_logo,
                navController = navController
            )
        }

        // ABOUT
        composable("about") {
            AboutScreen(navController = navController)
        }

        // TRUSTED CUSTOMER
        composable("add_customer_email") {
            AddTrustedCustomerScreen(navController)
        }

        composable(
            route = "add_customer_email?scannedEmail={scannedEmail}",
            arguments = listOf(
                navArgument("scannedEmail") {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) {
            AddTrustedCustomerScreen(navController)
        }

        // ADD EMPLOYEE QR
        composable("add_employee_qr") {
            AddEmployeeByQRScreen(navController)
        }

        // ADD EMPLOYEE CODE
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

        // ORDER DETAIL (TU FEATURE NUEVO)
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
    object Register : RootRoute("register")
    object Main : RootRoute("main_container")
}
