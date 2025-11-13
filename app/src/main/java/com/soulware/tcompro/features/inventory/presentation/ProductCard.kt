package com.soulware.tcompro.features.inventory.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.soulware.tcompro.features.inventory.domain.model.Product

@Composable
fun ProductCard(
    product: Product,
    onAdd: () -> Unit,
    onRemove: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(16.dp).background(MaterialTheme.colorScheme.surface)) {

            AsyncImage(
                model = product.imageUrl,
                contentDescription = product.name,
                modifier = Modifier.size(60.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(product.name, style = MaterialTheme.typography.titleMedium)
                product.description?.let {
                    Text(it, style = MaterialTheme.typography.bodySmall)
                }
                Text("S/. ${product.price}")
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Button(onClick = onAdd) { Text("Add") }
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = onRemove) { Text("Remove") }
            }
        }
    }
}
