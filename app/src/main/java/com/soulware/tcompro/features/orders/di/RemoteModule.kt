package com.soulware.tcompro.features.orders.di

import com.soulware.tcompro.features.orders.data.remote.services.OrderService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {

    @Provides
    @Singleton
    @Named("base_url")
    fun provideBaseUrl(): String = "http://20.201.98.91:8080/"

    @Provides
    @Singleton
    fun provideRetrofit(@Named("base_url") url: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideOrdersApiService(retrofit: Retrofit): OrderService {
        return retrofit.create(OrderService::class.java)
    }
}
