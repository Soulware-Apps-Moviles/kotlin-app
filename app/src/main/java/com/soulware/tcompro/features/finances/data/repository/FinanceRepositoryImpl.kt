package com.soulware.tcompro.features.finances.data.repository

import com.soulware.tcompro.features.finances.data.remote.services.FinanceService
import com.soulware.tcompro.features.finances.domain.models.Debt
import com.soulware.tcompro.features.finances.domain.models.Payment
import com.soulware.tcompro.features.finances.domain.repository.FinanceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FinanceRepositoryImpl @Inject constructor(
    private val service: FinanceService
) : FinanceRepository {

    override suspend fun getPayments(shopId: Int): List<Payment> = withContext(Dispatchers.IO) {
        try {
            service.getPayments(shopId)
        } catch (e: Exception) {
            e.printStackTrace()
            println("🔥 ERROR getPayments(): ${e.message}")
            emptyList()
        }
    }

    override suspend fun getDebts(shopId: Int, status: String): List<Debt> = withContext(Dispatchers.IO) {
        try {
            val response = service.getDebts(shopId = shopId, status = status)
            response
        } catch (e: Exception) {
            e.printStackTrace()
            println("🔥 ERROR getDebts(): ${e.message}")
            emptyList()
        }
    }

    override suspend fun markDebtAsPaid(debtId: Int) = withContext(Dispatchers.IO) {
        try {
            service.markDebtAsPaid(debtId)
        } catch (_: Exception) { }
    }
}
