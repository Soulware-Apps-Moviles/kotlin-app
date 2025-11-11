package com.soulware.tcompro.features.inventory.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soulware.tcompro.features.inventory.domain.model.Product
import com.soulware.tcompro.features.inventory.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products = _products.asStateFlow()

    init {
        getProducts()
    }

    fun getProducts() {
        viewModelScope.launch {
            try {
                _products.value = repository.getProducts()
            } catch (e: Exception) {
                // TODO: Handle error
            }
        }
    }

    fun addProduct(product: Product) {
        viewModelScope.launch {
            try {
                repository.addProductToInventory(product)
                getProducts()
            } catch (e: Exception) {
                // TODO: Handle error
            }
        }
    }

    fun removeProduct(productId: String) {
        viewModelScope.launch {
            try {
                repository.removeProductFromInventory(productId)
                getProducts()
            } catch (e: Exception) {
                // TODO: Handle error
            }
        }
    }
}