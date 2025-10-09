package com.soulware.tcompro.core

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable
fun <T : ITabRoute> TwoTabScreen(
    tabs: List<T>,
    content1: @Composable () -> Unit,
    content2: @Composable () -> Unit
) {
    require(tabs.size == 2) { "TwoTabScreen requires exactly two tabs." }

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val selectedIndex = tabs.indexOfFirst { it.route == currentRoute }.coerceAtLeast(0)

    // For selected
    val whiteBackground = MaterialTheme.colorScheme.surface
    val blackLetter = MaterialTheme.colorScheme.onSecondaryContainer

    // For non-selected
    val blackBackground = MaterialTheme.colorScheme.surfaceContainerHighest
    val whiteLetter = MaterialTheme.colorScheme.onPrimary

    Column {
        SecondaryTabRow(
            selectedTabIndex = selectedIndex,
            modifier = Modifier.fillMaxWidth()
        ) {
            tabs.forEach { tab ->
                val isSelected = tab.route == currentRoute

                val backgroundColor = if (isSelected) {
                    whiteBackground
                } else {
                    blackBackground
                }

                val letterColor = if (isSelected) {
                    blackLetter
                } else {
                    whiteLetter
                }

                Tab(
                    modifier = Modifier.background(backgroundColor),
                    selected = isSelected,
                    onClick = {
                        navController.navigate(tab.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    text = {
                        Text(
                            stringResource(tab.labelResId),
                            style = MaterialTheme.typography.bodyMedium,
                            color = letterColor,
                            fontSize = 12.sp
                        )
                    }
                )
            }
        }
        NavHost(
            navController,
            startDestination = tabs.first().route
        ) {
            composable(tabs[0].route) {
                content1()
            }
            composable(tabs[1].route) {
                content2()
            }
        }
    }
}