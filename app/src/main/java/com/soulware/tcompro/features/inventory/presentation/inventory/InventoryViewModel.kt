package com.soulware.tcompro.features.inventory.presentation.inventory

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
class InventoryViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    private val shopId = 10004

    private val _inventoryProducts = MutableStateFlow<List<Product>>(emptyList())
    val inventoryProducts = _inventoryProducts.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    init {
        getInventoryProducts()
    }

    fun getInventoryProducts() {
        viewModelScope.launch {
            try {
                _inventoryProducts.value = repository.getInventoryProducts(shopId)
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }

    fun addProductToInventory(product: Product) {
        viewModelScope.launch {
            repository.addProductToInventory(shopId, product)
            getInventoryProducts()
        }
    }

    fun removeProductFromInventory(productId: Int) {
        viewModelScope.launch {
            repository.removeProductFromInventory(shopId, productId)
            getInventoryProducts()
        }
    }
}
