package com.soulware.tcompro.features.inventory.presentation

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
fun NoProductsOnInventoryScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.no_orders_illustration),
            contentDescription = "No products in inventory",
            modifier = Modifier.size(200.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "No products in inventory",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            ),
            textAlign = TextAlign.Center
        )
    }
}
@Composable
fun InventoryProductsScreen(
    viewModel: ProductViewModel = hiltViewModel()
) {
    val products by viewModel.products.collectAsState(initial = emptyList())
    if (products.isEmpty()) {
        // Si está vacía, muestra la pantalla "sin productos"
        NoProductsOnInventoryScreen()
    } else {
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
}
