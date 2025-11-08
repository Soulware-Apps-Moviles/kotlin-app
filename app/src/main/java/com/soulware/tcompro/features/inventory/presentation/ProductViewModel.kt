package com.soulware.tcompro.features.inventory.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soulware.tcompro.features.inventory.domain.model.Product
import com.soulware.tcompro.features.inventory.domain.model.ProductCategory
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

    private fun getProducts() {
        // TODO: Replace with actual data fetching from the repository
        _products.value = listOf(
            Product("1", "Manzanas", 1.5, ProductCategory.PRODUCE, 100, "https://via.placeholder.com/150/FF0000/FFFFFF?Text=Manzanas"),
            Product("2", "Leche", 2.0, ProductCategory.DAIRY, 50, "https://via.placeholder.com/150/FFFFFF/000000?Text=Leche"),
            Product("3", "Pan", 1.0, ProductCategory.GROCERY, 200, "https://via.placeholder.com/150/DEB887/000000?Text=Pan")
        )
    }

    fun addProduct(product: Product) {
        // No-op for now
    }

    fun removeProduct(productId: String) {
        // No-op for now
    }
}