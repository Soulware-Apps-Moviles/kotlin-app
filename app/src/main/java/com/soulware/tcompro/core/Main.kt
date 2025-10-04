package com.soulware.tcompro.core

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AllInbox
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.soulware.tcompro.core.ui.theme.TcomproTheme

data class NavigationItem(
    val icon: ImageVector,
    val route: String
)

@Composable
fun Main(onClick: (Int) -> Unit){
    val selectedTab = remember {
        mutableStateOf(0)
    }

    val navigationItems = listOf(
        NavigationItem(icon = Icons.Default.AllInbox, route = "Orders"),
        NavigationItem(icon = Icons.Default.Inbox, route = "Inventory"),
        NavigationItem(icon = Icons.Default.Storefront, route = "Shop"),
        NavigationItem(icon = Icons.Default.Savings, route = "Finance"),
        NavigationItem(icon = Icons.Default.Settings, route = "Settings")
    )

    Scaffold(
        bottomBar = {
            BottomAppBar {
                navigationItems.forEachIndexed { index, item ->
                    val selected = index == selectedTab.value

                    NavigationBarItem(
                        selected = selected,
                        onClick = { selectedTab.value = index },
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = null,
                                tint = if (selected) Color(0xFFDD6529) else Color.Gray // aca no se porque no me deja llamar al color de nuestro theme, corregir luego
                            )
                        },
                        label = {
                            Text(
                                text = item.route,
                                color = if (selected) Color(0xFFDD6529) else Color.Gray
                            )
                        }
                    )
                }
            }
        }
    )
    {
        Column(
            modifier = Modifier.padding(it)
        ) {

        }
    }

}

@Preview(showBackground = true)
@Composable
fun MainPreview(){
    TcomproTheme {
        Main {  }
    }
}