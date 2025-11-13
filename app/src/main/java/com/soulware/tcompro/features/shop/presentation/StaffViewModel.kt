package com.soulware.tcompro.features.shop.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soulware.tcompro.core.data.SessionManager // <-- NUEVO IMPORT
import kotlinx.coroutines.flow.first
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
    private val repository: ShopRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(StaffScreenState(isLoading = true)) // Inicia cargando
    val uiState: StateFlow<StaffScreenState> = _uiState.asStateFlow()

    init {
        loadStaff()
    }

    fun loadStaff() {
        viewModelScope.launch {
            _uiState.value = StaffScreenState(isLoading = true)

            // --- ¡DEJAMOS DE SIMULAR! ---
            // 2. Leemos el shopId real de la sesión
            val shopId = sessionManager.userSessionFlow.first().shopId

            if (shopId == null) {
                // Si no hay shopId, no podemos cargar nada (error de sesión)
                _uiState.value = StaffScreenState(isLoading = false, employees = emptyList())
                return@launch
            }

            // 3. Usamos el shopId real
            val employees = repository.getShopkeepers(shopId = shopId.toString())
            // ---------------------------

            _uiState.value = StaffScreenState(isLoading = false, employees = employees)
        }
    }

    fun deleteEmployee(employeeId: Long) {
        viewModelScope.launch {
            // --- ¡DEJAMOS DE SIMULAR! ---
            val shopId = sessionManager.userSessionFlow.first().shopId
            if (shopId == null) { /* TODO: Error de session */ return@launch }

            val success = repository.deleteEmployee(shopId = shopId.toString(), shopkeeperId = employeeId)

            if (success) {
                loadStaff()
            } else {
                // TODO: Mostrar un mensaje de error al usuario
            }
        }
    }
}
