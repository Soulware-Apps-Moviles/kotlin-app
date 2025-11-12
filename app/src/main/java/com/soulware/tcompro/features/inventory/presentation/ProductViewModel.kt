package com.soulware.tcompro.features.inventory.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soulware.tcompro.features.inventory.domain.model.Product
import com.soulware.tcompro.features.inventory.domain.model.ProductCategory
import com.soulware.tcompro.features.inventory.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class SortOrder {
    NONE,
    BY_NAME_ASC,
    BY_NAME_DESC,
    BY_CATEGORY
}

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    private val _allProducts = MutableStateFlow<List<Product>>(emptyList())

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow<ProductCategory?>(null)
    val selectedCategory = _selectedCategory.asStateFlow()

    private val _sortOrder = MutableStateFlow(SortOrder.NONE)
    val sortOrder = _sortOrder.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    val products = combine(
        _allProducts,
        _searchQuery,
        _selectedCategory,
        _sortOrder
    ) { products, query, category, sortOrder ->
        products.filter { product ->
            (category == null || product.category == category) &&
                    (query.isBlank() || product.name.contains(query, ignoreCase = true))
        }.let { filteredProducts ->
            when (sortOrder) {
                SortOrder.BY_NAME_ASC -> filteredProducts.sortedBy { it.name }
                SortOrder.BY_NAME_DESC -> filteredProducts.sortedByDescending { it.name }
                SortOrder.BY_CATEGORY -> filteredProducts.sortedBy { it.category.name }
                SortOrder.NONE -> filteredProducts
            }
        }
    }

    init {
        getProducts()
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onCategoryChange(category: ProductCategory?) {
        _selectedCategory.value = category
    }

    fun onSortOrderChange(sortOrder: SortOrder) {
        _sortOrder.value = sortOrder
    }

    fun getProducts() {
        viewModelScope.launch {
            try {
                _allProducts.value = repository.getProducts()
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Error getting products"
            }
        }
    }

    fun addProduct(product: Product) {
        viewModelScope.launch {
            try {
                repository.addProductToInventory(product)
                getProducts()
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Error adding product to inventory"
            }
        }
    }

    fun removeProduct(productId: String) {
        viewModelScope.launch {
            try {
                repository.removeProductFromInventory(productId)
                getProducts()
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Error removing product from inventory"
            }
        }
    }
}