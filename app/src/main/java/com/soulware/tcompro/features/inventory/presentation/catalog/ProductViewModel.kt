package com.soulware.tcompro.features.inventory.presentation.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soulware.tcompro.features.inventory.domain.model.Product
import com.soulware.tcompro.features.inventory.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    private val shopId = 10004

    private val _allProducts = MutableStateFlow<List<Product>>(emptyList())

    private val _searchQuery = MutableStateFlow("")
    private val _errorMessage = MutableStateFlow<String?>(null)

    private val _inventoryProducts = MutableStateFlow<List<Product>>(emptyList())

    val inventoryProducts = _inventoryProducts.asStateFlow()

    val products = combine(
        _allProducts,
        _searchQuery
    ) { list, query ->
        list.filter { p ->
            p.name.contains(query, true) ||
                    (p.description?.contains(query, true) == true)
        }
    }

    init {
        getCatalogProducts()
        getInventoryProducts()
    }

    fun onSearchQueryChange(q: String) {
        _searchQuery.value = q
    }

    fun getCatalogProducts() {
        viewModelScope.launch {
            try {
                _allProducts.value = repository.getCatalogProducts()
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
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
            try {
                repository.addProductToInventory(shopId, product)
                getInventoryProducts()
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }

    fun removeProductFromInventory(productId: Int) {
        viewModelScope.launch {
            try {
                repository.removeProductFromInventory(shopId, productId)
                getInventoryProducts()
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }
}
