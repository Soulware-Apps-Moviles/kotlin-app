package com.soulware.tcompro.features.orders.presentation.orders.incoming

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
class IncomingOrdersViewModel @Inject constructor(
    private val repository: OrderRepository
) : ViewModel() {

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        getIncomingOrders()
    }

    fun getIncomingOrders() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                val result = repository.getIncomingOrders()
                _orders.value = result
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Unexpected error"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun acceptOrder(orderId: Int) {
        viewModelScope.launch {
            try {
                repository.acceptOrder(orderId)
                getIncomingOrders()
            } catch (e: Exception) {
                _errorMessage.value = "Failed to accept order"
            }
        }
    }

    fun rejectOrder(orderId: Int) {
        viewModelScope.launch {
            try {
                repository.rejectOrder(orderId)
                getIncomingOrders()
            } catch (e: Exception) {
                _errorMessage.value = "Failed to reject order"
            }
        }
    }
}
