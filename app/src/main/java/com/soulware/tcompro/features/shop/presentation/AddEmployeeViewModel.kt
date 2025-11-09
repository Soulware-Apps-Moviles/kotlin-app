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
import androidx.lifecycle.SavedStateHandle
import com.soulware.tcompro.core.data.SessionManager // <-- NUEVO IMPORT
import kotlinx.coroutines.flow.first // <-- NUEVO IMPORT
enum class ValidationStatus { IDLE, LOADING, SUCCESS, ERROR }

data class AddEmployeeState(
    val status: ValidationStatus = ValidationStatus.IDLE,
    val foundEmployee: Employee? = null,
    val errorMessage: String = ""
)

@HiltViewModel
class AddEmployeeViewModel @Inject constructor(
    private val repository: ShopRepository,
    private val sessionManager: SessionManager,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddEmployeeState())
    val uiState: StateFlow<AddEmployeeState> = _uiState.asStateFlow()
    init {
        val scannedCode: String? = savedStateHandle.get("scannedCode")

        if (scannedCode != null) {
            validateAndHireEmployee(scannedCode)
        }
    }

    fun validateAndHireEmployee(code: String) {
        val authId = code

        viewModelScope.launch {
            _uiState.value = AddEmployeeState(status = ValidationStatus.LOADING)

            // --- ¡DEJAMOS DE SIMULAR! ---
            val shopId = sessionManager.userSessionFlow.first().shopId
            if (shopId == null) {
                _uiState.value = AddEmployeeState(status = ValidationStatus.ERROR, errorMessage = "Session error, no Shop ID found.")
                return@launch
            }

            // 2. Usamos el shopId real
            val employee = repository.hireShopkeeper(shopId = shopId.toString(), authId = authId)
            // ---------------------------

            if (employee != null) {
                _uiState.value = AddEmployeeState(
                    status = ValidationStatus.SUCCESS,
                    foundEmployee = employee
                )
            } else {
                _uiState.value = AddEmployeeState(
                    status = ValidationStatus.ERROR,
                    errorMessage = "no user with that code"
                )
            }
        }
    }

    fun resetState() {
        _uiState.value = AddEmployeeState(status = ValidationStatus.IDLE)
    }
}