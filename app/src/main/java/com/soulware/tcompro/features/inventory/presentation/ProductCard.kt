package com.soulware.tcompro.features.inventory.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.soulware.tcompro.features.inventory.domain.model.Product

@Composable
fun ProductCard(
    product: Product,
    onAdd: (() -> Unit)? = null,
    onRemove: (() -> Unit)? = null
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(0.dp).background(MaterialTheme.colorScheme.surface)) {

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
                onAdd?.let {
                    Button(onClick = it) { Text("Add") }
                }
                onRemove?.let {
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = it) { Text("Remove") }
                }
            }
        }
    }
}
