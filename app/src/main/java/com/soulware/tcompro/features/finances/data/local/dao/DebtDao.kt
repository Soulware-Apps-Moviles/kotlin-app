package com.soulware.tcompro.features.finances.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.soulware.tcompro.features.finances.data.local.models.DebtEntity

@Dao
interface DebtDao {

    @Query("SELECT * FROM debts WHERE shopId = :shopId AND status = :status")
    suspend fun getDebts(shopId: Int, status: String): List<DebtEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDebts(debts: List<DebtEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDebt(debt: DebtEntity)

    @Query("UPDATE debts SET status = :newStatus WHERE id = :id")
    suspend fun updateStatus(id: Int, newStatus: String)

    @Query("DELETE FROM debts")
    suspend fun clearDebts()
}
