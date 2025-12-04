package com.soulware.tcompro.features.inventory.di

import com.soulware.tcompro.features.inventory.data.remote.service.ProductApi
import com.soulware.tcompro.features.inventory.data.repository.ProductRepositoryImpl
import com.soulware.tcompro.features.inventory.domain.repository.ProductRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object InventoryModule {

    @Provides
    @Singleton
    fun provideProductRepository(
        api: ProductApi
    ): ProductRepository {
        return ProductRepositoryImpl(api)
    }
}
