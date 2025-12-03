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

enum class AuthStatus {
    IDLE, LOADING, SUCCESS_LOGIN, SUCCESS_LOGIN_NO_SHOP, SUCCESS_REGISTER, ERROR
}

data class AuthUiState(
    val status: AuthStatus = AuthStatus.IDLE,
    val errorMessage: String = "",
    val userEmail: String = ""
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
                if (result.role == "SHOP_OWNER" && (result.shopId == 0L || result.shopId == null)) {
                    _uiState.value = AuthUiState(
                        status = AuthStatus.SUCCESS_LOGIN_NO_SHOP,
                        userEmail = email
                    )
                } else {
                    // Todo normal -> Ir al Home
                    _uiState.value = AuthUiState(status = AuthStatus.SUCCESS_LOGIN)
                }
            } else {
                _uiState.value = AuthUiState(
                    status = AuthStatus.ERROR,
                    errorMessage = "Credenciales inválidas o error de red."
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
                    errorMessage = "No se pudo crear la cuenta."
                )
            }
        }
    }
}