package com.soulware.tcompro.features.orders.presentation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.soulware.tcompro.core.ui.theme.TcomproTheme

@Composable
fun Order(
    viewModel: OrderViewModel = hiltViewModel(),
    onSubmit: () -> Unit
){
    Text("ORDERS")
}

@Preview
@Composable
fun OrderPreview(){
    TcomproTheme {
        Order {  }
    }
}