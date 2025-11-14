package com.soulware.tcompro.features.orders.di

import com.soulware.tcompro.features.orders.data.repository.OrdersRepositoryImpl
import com.soulware.tcompro.features.orders.domain.repository.OrderRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface RepositoryModule {

    @Binds
    fun bindOrdersRepository(impl: OrdersRepositoryImpl): OrderRepository
}
