package com.soulware.tcompro.features.orders.presentation.orderdetail

import android.widget.Space
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.soulware.tcompro.core.ui.theme.TcomproTheme
import com.soulware.tcompro.features.orders.data.ProductItem

@Composable
fun ProductItem(
    productItem: ProductItem
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        AsyncImage(
            model = productItem.image,
            contentDescription = productItem.name,
            modifier = Modifier
                .size(40.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = productItem.name,
            fontSize = 10.sp,
            modifier = Modifier.weight(1f)
        )

        Text(text = "x${productItem.quantity}", fontSize = 10.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun ProductItemPreview(){
    TcomproTheme {
        ProductItem(
            ProductItem("https://...", "Inca Kola 1.5L", 1)
        )
    }
}