package com.soulware.tcompro.features.shop.presentation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun Shop(
    viewModel: ShopViewModel = hiltViewModel(),
    onSubmit: () -> Unit
){
    Text("SHOP")
}