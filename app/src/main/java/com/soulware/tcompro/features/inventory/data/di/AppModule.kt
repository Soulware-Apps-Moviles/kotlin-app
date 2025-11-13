package com.soulware.tcompro.features.inventory.data.di

import com.soulware.tcompro.features.inventory.data.remote.service.ProductApi
import com.soulware.tcompro.features.inventory.data.di.ProductRepositoryImpl
import com.soulware.tcompro.features.inventory.domain.repository.ProductRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideProductApi(): ProductApi {
        return Retrofit.Builder()
            .baseUrl("http://20.201.98.91:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProductApi::class.java)
    }

    @Provides
    @Singleton
    fun provideProductRepository(api: ProductApi): ProductRepository {
        return ProductRepositoryImpl(api)
    }
}