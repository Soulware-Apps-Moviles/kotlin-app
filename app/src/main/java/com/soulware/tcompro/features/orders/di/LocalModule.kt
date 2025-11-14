package com.soulware.tcompro.features.orders.di

import android.app.Application
import androidx.room.Room
import com.soulware.tcompro.features.orders.data.local.dao.OrderDao
import com.soulware.tcompro.features.orders.data.local.dao.OrderLineDao
import com.soulware.tcompro.features.orders.data.local.database.AppDatabase
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
            "tcompro"
        ).build()
    }

    @Provides
    @Singleton
    fun provideOrderDao(appDatabase: AppDatabase): OrderDao {
        return appDatabase.orderDao()
    }

    @Provides
    @Singleton
    fun provideOrderLineDao(appDatabase: AppDatabase): OrderLineDao {
        return appDatabase.orderLineDao()
    }
}
