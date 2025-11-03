package com.soulware.tcompro.features.shop.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soulware.tcompro.features.shop.data.ShopRepository
import com.soulware.tcompro.features.shop.domain.Employee
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


data class StaffScreenState(
    val isLoading: Boolean = false,
    val employees: List<Employee> = emptyList()
)

@HiltViewModel
class StaffViewModel @Inject constructor(
    private val repository: ShopRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(StaffScreenState(isLoading = true)) // Inicia cargando
    val uiState: StateFlow<StaffScreenState> = _uiState.asStateFlow()

    init {
        loadStaff()
    }

    fun loadStaff() {
        viewModelScope.launch {
            _uiState.value = StaffScreenState(isLoading = true)


            val employees = repository.getShopkeepers(shopId = "1")
            // --------------------

            _uiState.value = StaffScreenState(isLoading = false, employees = employees)
        }
    }
    fun deleteEmployee(employeeId: Long) {
        viewModelScope.launch {
            val success = repository.deleteEmployee(shopId = "1", shopkeeperId = employeeId)

            if (success) {
                loadStaff()
            } else {
            }
        }
    }
}