package com.soulware.tcompro.features.inventory.presentation

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

@Composable
fun CatalogScreen(
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