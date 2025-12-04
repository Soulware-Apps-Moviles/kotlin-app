package com.soulware.tcompro.features.orders.presentation.orders.pending

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soulware.tcompro.features.orders.domain.models.Order
import com.soulware.tcompro.features.orders.domain.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PendingOrdersViewModel @Inject constructor(
    private val repository: OrderRepository
) : ViewModel() {

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private var autoRefreshJob: Job? = null

    init {
        startAutoRefresh()
    }

    fun startAutoRefresh() {
        if (autoRefreshJob?.isActive == true) return

        autoRefreshJob = viewModelScope.launch {
            while (true) {
                try {
                    getPendingOrders(silent = true)
                } catch (e: Exception) {
                    _errorMessage.value = e.message ?: "Error refreshing pending orders"
                }
                delay(5000)
            }
        }
    }

    fun stopAutoRefresh() {
        autoRefreshJob?.cancel()
        autoRefreshJob = null
    }

    fun getPendingOrders(silent: Boolean = false) {
        viewModelScope.launch {
            if (!silent) _isLoading.value = true
            _errorMessage.value = null

            try {
                val result = repository.getPendingOrders()
                _orders.value = result
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Unexpected error"
            } finally {
                if (!silent) _isLoading.value = false
            }
        }
    }

    fun advanceOrder(order: Order) {
        viewModelScope.launch {
            try {
                val times = when (order.pickupMethod) {
                    "SHOP_PICKUP" -> 2 // ACCEPTED → READY → PICKED_UP
                    "DELIVERY" -> 3    // ACCEPTED → READY → DISPATCHED → DELIVERED
                    else -> 2
                }

                repeat(times) {
                    repository.advanceOrder(order.id)
                }

                getPendingOrders()
            } catch (e: Exception) {
                _errorMessage.value = "Failed to dispatch order"
            }
        }
    }


    fun cancelOrder(orderId: Int) {
        viewModelScope.launch {
            try {
                repository.cancelOrder(orderId)
                getPendingOrders()
            } catch (e: Exception) {
                _errorMessage.value = "Failed to cancel order"
            }
        }
    }
}
