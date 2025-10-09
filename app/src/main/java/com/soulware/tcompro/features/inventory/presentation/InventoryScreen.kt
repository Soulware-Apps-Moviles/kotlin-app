package com.soulware.tcompro.features.inventory.presentation

import androidx.annotation.StringRes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.soulware.tcompro.R
import com.soulware.tcompro.core.ITabRoute
import com.soulware.tcompro.core.TwoTabScreen

sealed class InventoryInnerTabRoute(
    override val route: String,
    @get:StringRes override val labelResId: Int
) : ITabRoute {
    object MyInventory : InventoryInnerTabRoute("inventory_products_tab", R.string.label_inventory_products)
    object Catalog : InventoryInnerTabRoute("catalog_products_tab", R.string.label_catalog_products)
}

@Composable
fun InventoryScreen() {
    val inventoryTabs = listOf(
        InventoryInnerTabRoute.MyInventory,
        InventoryInnerTabRoute.Catalog
    )
    TwoTabScreen(
        tabs = inventoryTabs,
        content1 = { Text(stringResource(R.string.placeholder_inventory)) },
        content2 = { Text(stringResource(R.string.placeholder_catalog)) }
    )
}