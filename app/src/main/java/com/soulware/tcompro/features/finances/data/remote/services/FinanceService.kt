package com.soulware.tcompro.features.finances.data.remote.services

import com.soulware.tcompro.features.finances.data.remote.models.DebtDto
import com.soulware.tcompro.features.finances.data.remote.models.PaymentDto
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface FinanceService {
    @GET("payments/v1")
    suspend fun getPayments(
        @Query("shopId") shopId: Long
    ): List<PaymentDto>

    @GET("debts/v1")
    suspend fun getDebts(
        @Query("shopId") shopId: Long,
        @Query("status") status: String? = null
    ): List<DebtDto>

    @POST("debts/v1/{id}/paid")
    suspend fun markDebtAsPaid(@Path("id") debtId: Int)
}

