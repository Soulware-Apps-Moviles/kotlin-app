package com.soulware.tcompro.features.orders.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soulware.tcompro.features.orders.domain.models.Order
import com.soulware.tcompro.features.orders.domain.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderDetailViewModel @Inject constructor(
    private val repository: OrderRepository
) : ViewModel() {

    private val _order = MutableStateFlow<Order?>(null)
    val order: StateFlow<Order?> = _order.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun loadOrder(orderId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val result = repository.getOrderById(orderId)
                _order.value = result
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Unexpected error"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun cancelOrder(orderId: Int) {
        viewModelScope.launch {
            try {
                repository.cancelOrder(orderId)
            } catch (e: Exception) {
                _errorMessage.value = "Failed to cancel order"
            }
        }
    }

    fun advanceOrder(orderId: Int) {
        viewModelScope.launch {
            try {
                repository.advanceOrder(orderId)
            } catch (e: Exception) {
                _errorMessage.value = "Failed to update order status"
            }
        }
    }
}
