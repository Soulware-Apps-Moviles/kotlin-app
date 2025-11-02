package com.soulware.tcompro.features.inventory.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.soulware.tcompro.features.inventory.domain.model.Product
import com.soulware.tcompro.features.inventory.domain.model.ProductCategory

@Composable
fun CatalogScreen() {
    val sampleProducts = listOf(
        Product("1", "Manzanas", 1.5, ProductCategory.PRODUCE, 100),
        Product("2", "Leche", 2.0, ProductCategory.DAIRY, 50),
        Product("3", "Pan", 1.0, ProductCategory.GROCERY, 200)
    )
    LazyColumn(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(sampleProducts) { product ->
            ProductCard(product = product)
        }
    }
}