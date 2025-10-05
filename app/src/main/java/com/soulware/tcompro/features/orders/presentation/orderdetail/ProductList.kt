package com.soulware.tcompro.features.orders.presentation.orderdetail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.soulware.tcompro.core.ui.theme.TcomproTheme
import com.soulware.tcompro.features.orders.data.ProductItem

@Composable
fun ProductList(
    viewModel: ProductListViewModel = hiltViewModel()
){
    val products = viewModel.products.collectAsState()
    val isVisible = viewModel.isVisible.collectAsState()

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable{ viewModel.toggleVisible() }
                .padding(vertical = 8.dp)
        ){
            Text(
                text = "See product list",
                fontSize = 12.sp,
                modifier = Modifier.weight(1f)
            )
            Icon(imageVector = if (isVisible.value) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = null)
        }
        if (isVisible.value){
            products.value.forEach { product ->
                ProductItem(
                    ProductItem(product.image,product.name,product.quantity)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductListPreview(){
    TcomproTheme {
        ProductList()
    }
}