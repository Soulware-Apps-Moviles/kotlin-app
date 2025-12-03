package com.soulware.tcompro.features.shop.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Query
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
    val employees: List<Employee> = emptyList(),
    val searchQuery: String=""
)

@HiltViewModel
class StaffViewModel @Inject constructor(
    private val repository: ShopRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(StaffScreenState(isLoading = true)) // Inicia cargando
    val uiState: StateFlow<StaffScreenState> = _uiState.asStateFlow()

    private var allEmployees: List<Employee> = emptyList()

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
// Guardamos la lista completa en nuestra variable privada
            allEmployees = repository.getShopkeepers(shopId.toString())

            // Al cargar, mostramos la lista completa (filtrando por si ya había texto escrito)
            filterEmployees(_uiState.value.searchQuery)
        }
    }
    fun onSearchQueryChanged(query: String) {
        filterEmployees(query)
    }
    private fun filterEmployees(query: String) {
        val filteredList = if (query.isBlank()) {
            allEmployees
        } else {
            allEmployees.filter { it.name.contains(query, ignoreCase = true) }
        }

        _uiState.value = _uiState.value.copy(
            searchQuery = query,
            employees = filteredList,
            isLoading = false
        )
    }

    fun deleteEmployee(employeeId: Long) {
        viewModelScope.launch {
            val shopId = sessionManager.userSessionFlow.first().shopId ?: return@launch

            _uiState.value = _uiState.value.copy(isLoading = true)

            val success = repository.deleteEmployee(shopId.toString(), employeeId)

            if (success) {
                loadStaff()
            } else {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }
}
