package com.soulware.tcompro.features.finances.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soulware.tcompro.features.finances.domain.models.Debt
import com.soulware.tcompro.features.finances.domain.models.Payment
import com.soulware.tcompro.features.finances.domain.repository.FinanceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FinancesViewModel @Inject constructor(
    private val repository: FinanceRepository
) : ViewModel() {

    private val shopId = 10004

    private val _payments = MutableStateFlow<List<Payment>>(emptyList())
    val payments: StateFlow<List<Payment>> = _payments.asStateFlow()

    private val _debts = MutableStateFlow<List<Debt>>(emptyList())
    val debts: StateFlow<List<Debt>> = _debts.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _totalRevenue = MutableStateFlow(0.0)
    val totalRevenue: StateFlow<Double> = _totalRevenue.asStateFlow()

    init {
        loadFinances()
    }

    fun loadFinances() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _errorMessage.value = null

                val paymentsList = repository.getPayments(shopId)

                val debtsList = repository.getDebts(shopId, status = "PAID")

                _payments.value = paymentsList
                _debts.value = debtsList

                _totalRevenue.value = paymentsList.sumOf { it.amount }

            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Unexpected error"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun markDebtAsPaid(debtId: Int) {
        viewModelScope.launch {
            try {
                repository.markDebtAsPaid(debtId)

                _debts.value = _debts.value.map { debt ->
                    if (debt.id == debtId) debt.copy(status = "PAID") else debt
                }
            } catch (e: Exception) {
                _errorMessage.value = "Failed to mark as paid"
            }
        }
    }
}
