package com.soulware.tcompro.features.shop.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soulware.tcompro.core.data.SessionManager
import com.soulware.tcompro.features.shop.data.ShopRepository
import com.soulware.tcompro.features.shop.domain.Employee
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class ValidationStatus { IDLE, LOADING, SUCCESS, ERROR }

data class AddEmployeeState(
    val status: ValidationStatus = ValidationStatus.IDLE,
    val foundEmployee: Employee? = null,
    val errorMessage: String = ""
)

@HiltViewModel
class AddEmployeeViewModel @Inject constructor(
    private val repository: ShopRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddEmployeeState())
    val uiState: StateFlow<AddEmployeeState> = _uiState.asStateFlow()

    // Función para contratar usando EMAIL
    fun hireEmployeeByEmail(email: String) {
        if (email.isBlank()) {
            _uiState.value = AddEmployeeState(status = ValidationStatus.ERROR, errorMessage = "El correo no puede estar vacío")
            return
        }

        viewModelScope.launch {
            _uiState.value = AddEmployeeState(status = ValidationStatus.LOADING)

            val shopId = sessionManager.userSessionFlow.first().shopId
            if (shopId == null || shopId == 0L) {
                _uiState.value = AddEmployeeState(status = ValidationStatus.ERROR, errorMessage = "Error: No se encontró ID de tienda en sesión.")
                return@launch
            }

            // Llamamos a la nueva función del repositorio
            val employee = repository.hireShopkeeperByEmail(shopId.toString(), email)

            if (employee != null) {
                _uiState.value = AddEmployeeState(
                    status = ValidationStatus.SUCCESS,
                    foundEmployee = employee
                )
            } else {
                _uiState.value = AddEmployeeState(
                    status = ValidationStatus.ERROR,
                    errorMessage = "No se encontró un empleado registrado con ese correo."
                )
            }
        }
    }

    fun resetState() {
        _uiState.value = AddEmployeeState(status = ValidationStatus.IDLE)
    }
}