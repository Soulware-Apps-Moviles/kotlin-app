package com.soulware.tcompro.features.orders.presentation.orderdetail

import androidx.lifecycle.ViewModel
import com.soulware.tcompro.features.orders.data.ProductItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor() : ViewModel(){
    private val _products = MutableStateFlow<List<ProductItem>>(listOf(
        ProductItem("https://...", "Inca Kola 1.5L", 1),
        ProductItem("https://...", "Don Vittorio Spaghetti 450gr", 2),
        ProductItem("https://...", "Don Vittorio Salsa Roja 400gr", 1)
    ))
    val products: StateFlow<List<ProductItem>> = _products

    private val _isVisible = MutableStateFlow(false)
    val isVisible: StateFlow<Boolean> = _isVisible

    fun toggleVisible(){
        _isVisible.value = !_isVisible.value
    }
}