package com.soulware.tcompro.features.finances.data.repository

import com.soulware.tcompro.features.finances.data.local.dao.DebtDao
import com.soulware.tcompro.features.finances.data.local.dao.PaymentDao
import com.soulware.tcompro.features.finances.data.local.models.DebtEntity
import com.soulware.tcompro.features.finances.data.local.models.PaymentEntity
import com.soulware.tcompro.features.finances.data.remote.models.DebtDto
import com.soulware.tcompro.features.finances.data.remote.models.PaymentDto
import com.soulware.tcompro.features.finances.data.remote.services.FinanceService
import com.soulware.tcompro.features.finances.domain.models.Debt
import com.soulware.tcompro.features.finances.domain.models.Payment
import com.soulware.tcompro.features.finances.domain.repository.FinanceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

class FinanceRepositoryImpl @Inject constructor(
    private val service: FinanceService,
    private val paymentDao: PaymentDao,
    private val debtDao: DebtDao
) : FinanceRepository {

    override suspend fun getPayments(shopId: Int): List<Payment> =
        withContext(Dispatchers.IO) {
            try {
                val remotePayments = service.getPayments(shopId)
                val entities = remotePayments.map { it.toEntity() }

                paymentDao.clearPayments()
                paymentDao.insertPayments(entities)

                paymentDao.getPayments(shopId).map { it.toDomain() }

            } catch (e: Exception) {
                e.printStackTrace()
                println("ERROR getPayments(): ${e.message}")

                paymentDao.getPayments(shopId).map { it.toDomain() }
            }
        }

    override suspend fun getDebts(shopId: Int, status: String): List<Debt> =
        withContext(Dispatchers.IO) {
            try {
                val remoteDebts = service.getDebts(shopId = shopId, status = status)
                val entities = remoteDebts.map { it.toEntity() }

                debtDao.insertDebts(entities)

                debtDao.getDebts(shopId, status).map { it.toDomain() }

            } catch (e: Exception) {
                e.printStackTrace()
                println("ERROR getDebts(): ${e.message}")

                debtDao.getDebts(shopId, status).map { it.toDomain() }
            }
        }

    override suspend fun markDebtAsPaid(debtId: Int) =
        withContext(Dispatchers.IO) {
            try {
                service.markDebtAsPaid(debtId)
                debtDao.updateStatus(debtId, "PAID")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    override suspend fun payDebt(debt: Debt): Payment =
        withContext(Dispatchers.IO) {
            try {
                service.markDebtAsPaid(debt.id)
                debtDao.updateStatus(debt.id, "PAID")
                val paymentEntity = PaymentEntity(
                    id = UUID.randomUUID().hashCode(),
                    customerId = debt.customerId,
                    orderId = debt.orderId,
                    amount = debt.amount,
                    paymentMethodId = 1,
                    shopId = debt.shopId,
                    timestamp = System.currentTimeMillis()
                )
                paymentDao.insertPayment(paymentEntity)
                paymentEntity.toDomain()
            } catch (e: Exception) {
                e.printStackTrace()
                throw e
            }
        }
}

fun PaymentDto.toEntity(): PaymentEntity {
    return PaymentEntity(
        id = id,
        customerId = customerId,
        orderId = orderId,
        amount = amount,
        paymentMethodId = paymentMethodId,
        shopId = shopId,
        timestamp = System.currentTimeMillis()
    )
}

fun PaymentEntity.toDomain(): Payment {
    return Payment(
        id = id,
        customerId = customerId,
        orderId = orderId,
        amount = amount,
        paymentMethodId = paymentMethodId,
        shopId = shopId
    )
}

fun DebtDto.toEntity(): DebtEntity {
    return DebtEntity(
        id = id,
        customerId = customerId,
        shopId = shopId,
        orderId = orderId,
        amount = amount,
        status = status
    )
}

fun DebtEntity.toDomain(): Debt {
    return Debt(
        id = id,
        customerId = customerId,
        shopId = shopId,
        orderId = orderId,
        amount = amount,
        status = status
    )
}
