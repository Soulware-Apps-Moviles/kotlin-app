package com.soulware.tcompro.features.inventory.presentation.inventory

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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
import com.soulware.tcompro.features.inventory.presentation.ProductCard
import com.soulware.tcompro.features.inventory.presentation.catalog.CatalogScreen
import com.soulware.tcompro.features.inventory.presentation.catalog.NoProductsInCatalogScreen
import com.soulware.tcompro.features.inventory.presentation.inventory.InventoryViewModel

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
    viewModel: InventoryViewModel = hiltViewModel()
) {
    val products by viewModel.inventoryProducts.collectAsState()

    var searchQuery by remember { mutableStateOf("") }

    val filteredProducts = products.filter { product ->
        product.name.contains(searchQuery, ignoreCase = true) ||
                (product.description?.contains(searchQuery, ignoreCase = true) == true)
    }

    Column(modifier = Modifier.fillMaxSize()) {

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Search by name or description") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search"
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        if (products.isEmpty()) {
            NoProductsOnInventoryScreen()
        } else {
            LazyColumn(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(products) { product ->
                    ProductCard(
                        product = product,
                        onRemove = { viewModel.removeProductFromInventory(product.id) },
                        //onUpdatePrice = { newPrice -> viewModel.updateProductPrice(product.id, newPrice) }
                    )
                }
            }
        }
    }
}
