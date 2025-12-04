package com.soulware.tcompro.features.finances.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.soulware.tcompro.features.finances.data.local.models.PaymentEntity

@Dao
interface PaymentDao {
    @Query("SELECT * FROM payments WHERE shopId = :shopId")
    suspend fun getPayments(shopId: Long): List<PaymentEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayments(payments: List<PaymentEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayment(payment: PaymentEntity)

    @Query("DELETE FROM payments")
    suspend fun clearPayments()
}
