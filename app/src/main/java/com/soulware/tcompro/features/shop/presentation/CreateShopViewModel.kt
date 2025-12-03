package com.soulware.tcompro.features.shop.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soulware.tcompro.features.shop.data.ShopRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class CreateShopStatus { IDLE, LOADING, SUCCESS, ERROR }

data class CreateShopUiState(
    val status: CreateShopStatus = CreateShopStatus.IDLE,
    val errorMessage: String = ""
)

@HiltViewModel
class CreateShopViewModel @Inject constructor(
    private val repository: ShopRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateShopUiState())
    val uiState: StateFlow<CreateShopUiState> = _uiState.asStateFlow()

    fun createShop(name: String, lat: Double, lng: Double, ownerEmail: String) {
        viewModelScope.launch {
            _uiState.value = CreateShopUiState(status = CreateShopStatus.LOADING)

            val success = repository.createShopForOwner(name, lat, lng, ownerEmail)

            if (success) {
                _uiState.value = CreateShopUiState(status = CreateShopStatus.SUCCESS)
            } else {
                _uiState.value = CreateShopUiState(
                    status = CreateShopStatus.ERROR,
                    errorMessage = "Error al conectar con el servidor."
                )
            }
        }
    }
}