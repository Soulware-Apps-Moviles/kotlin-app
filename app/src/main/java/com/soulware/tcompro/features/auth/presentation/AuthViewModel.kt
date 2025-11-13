/*
 * AuthViewModel (ViewModel de Autenticación)
 *
 * Esta clase es el "Cerebro" (ViewModel) para las pantallas de Login y Register.
 * Se encarga de manejar la lógica de negocio y el estado de la UI para la autenticación.
 *
 * Funcionalidades:
 * - Define los estados de la UI (AuthStatus: IDLE, LOADING, SUCCESS, ERROR).
 * - Expone un 'uiState' (StateFlow) que la UI puede observar para reaccionar a cambios.
 * - Inyecta 'AuthRepository' para ejecutar las acciones de login y register.
 * - Llama a 'repository.login()' y actualiza el estado a SUCCESS_LOGIN o ERROR.
 * - Llama a 'repository.register()' y actualiza el estado a SUCCESS_REGISTER o ERROR.
 * - Inyecta 'SessionManager' (para ser usado en futuras funciones como logout).
 */
package com.soulware.tcompro.features.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soulware.tcompro.core.data.SessionManager
import com.soulware.tcompro.features.auth.data.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class AuthStatus { IDLE, LOADING, SUCCESS_LOGIN, SUCCESS_REGISTER, ERROR }

data class AuthUiState(
    val status: AuthStatus = AuthStatus.IDLE,
    val errorMessage: String = ""
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun login(email: String, pass: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState(status = AuthStatus.LOADING)

            val result = repository.login(email, pass)

            if (result != null) {
                _uiState.value = AuthUiState(status = AuthStatus.SUCCESS_LOGIN)
            } else {
                _uiState.value = AuthUiState(
                    status = AuthStatus.ERROR,
                    errorMessage = "Invalid credentials or network error."
                )
            }
        }
    }

    fun register(
        email: String,
        pass: String,
        firstName: String,
        lastName: String,
        phone: String
    ) {
        viewModelScope.launch {
            _uiState.value = AuthUiState(status = AuthStatus.LOADING)

            val success = repository.register(email, pass, firstName, lastName, phone)

            if (success) {
                _uiState.value = AuthUiState(status = AuthStatus.SUCCESS_REGISTER)
            } else {
                _uiState.value = AuthUiState(
                    status = AuthStatus.ERROR,
                    errorMessage = "Could not create account. User may already exist."
                )
            }
        }
    }
}