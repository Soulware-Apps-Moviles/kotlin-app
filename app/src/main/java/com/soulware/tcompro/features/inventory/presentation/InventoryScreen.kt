package com.soulware.tcompro.features.inventory.presentation

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
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
        content1 = { InventoryProductsScreen() },
        content2 = { CatalogScreen() }
    )
}

@Composable
fun InventoryProductsScreen(
    viewModel: ProductViewModel = hiltViewModel()
) {
    val products by viewModel.products.collectAsState()
    LazyColumn(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(products) { product ->
            ProductCard(
                product = product,
                onAdd = { viewModel.addProduct(product) },
                onRemove = { viewModel.removeProduct(product.id) }
            )
        }
    }
}