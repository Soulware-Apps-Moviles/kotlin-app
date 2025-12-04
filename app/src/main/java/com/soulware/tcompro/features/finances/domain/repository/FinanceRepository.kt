package com.soulware.tcompro.features.finances.domain.repository

import com.soulware.tcompro.features.finances.domain.models.Debt
import com.soulware.tcompro.features.finances.domain.models.Payment

interface FinanceRepository {

    suspend fun getPayments(): List<Payment>

    suspend fun getDebts(status: String): List<Debt>

    suspend fun markDebtAsPaid(debtId: Int)

    suspend fun payDebt(debt: Debt): Payment
}
