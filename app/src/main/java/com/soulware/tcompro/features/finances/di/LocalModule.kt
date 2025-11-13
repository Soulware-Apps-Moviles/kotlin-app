package com.soulware.tcompro.features.finances.di

import android.app.Application
import androidx.room.Room
import com.soulware.tcompro.features.finances.data.local.dao.PaymentDao
import com.soulware.tcompro.features.finances.data.local.dao.DebtDao
import com.soulware.tcompro.features.finances.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Provides
    @Singleton
    fun provideAppDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(
            application.applicationContext,
            AppDatabase::class.java,
            "tcompro-finances"
        ).build()
    }

    @Provides
    @Singleton
    fun providePaymentDao(db: AppDatabase): PaymentDao {
        return db.paymentDao()
    }

    @Provides
    @Singleton
    fun provideDebtDao(db: AppDatabase): DebtDao {
        return db.debtDao()
    }
}
