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
enum class ValidationStatus { IDLE, LOADING, SUCCESS, ERROR }

data class AddEmployeeState(
    val status: ValidationStatus = ValidationStatus.IDLE,
    val foundEmployee: Employee? = null,
    val errorMessage: String = ""
)

@HiltViewModel
class AddEmployeeViewModel @Inject constructor(
    private val repository: ShopRepository,
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
        val shopId = "1"

        viewModelScope.launch {
            _uiState.value = AddEmployeeState(status = ValidationStatus.LOADING)

            val employee = repository.hireShopkeeper(shopId, authId)

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