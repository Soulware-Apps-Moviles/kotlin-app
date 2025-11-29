
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
        phone: String,
        role: String
    ) {
        viewModelScope.launch {
            _uiState.value = AuthUiState(status = AuthStatus.LOADING)

            val success = repository.register(email, pass, firstName, lastName, phone, role)

            if (success) {
                _uiState.value = AuthUiState(status = AuthStatus.SUCCESS_REGISTER)
            } else {
                _uiState.value = AuthUiState(
                    status = AuthStatus.ERROR,
                    errorMessage = "Error al crear cuenta. Verifique los datos."
                )
            }
        }
    }

}