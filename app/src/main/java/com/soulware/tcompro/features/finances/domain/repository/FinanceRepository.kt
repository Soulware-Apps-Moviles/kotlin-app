package com.soulware.tcompro.features.finances.domain.repository

import com.soulware.tcompro.features.finances.domain.models.Debt
import com.soulware.tcompro.features.finances.domain.models.Payment

interface FinanceRepository {

    suspend fun getPayments(shopId: Int): List<Payment>

    suspend fun getDebts(shopId: Int, status: String): List<Debt>

    suspend fun markDebtAsPaid(debtId: Int)
}