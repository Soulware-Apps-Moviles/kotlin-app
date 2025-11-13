package com.soulware.tcompro.features.finances.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.soulware.tcompro.features.finances.data.local.dao.DebtDao
import com.soulware.tcompro.features.finances.data.local.dao.PaymentDao
import com.soulware.tcompro.features.finances.data.local.models.DebtEntity
import com.soulware.tcompro.features.finances.data.local.models.PaymentEntity

@Database(entities =  [PaymentEntity::class, DebtEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun debtDao(): DebtDao
    abstract fun paymentDao(): PaymentDao
}