package com.soulware.tcompro.features.orders.di

import com.soulware.tcompro.core.di.TcomproApi
import com.soulware.tcompro.features.orders.data.remote.services.OrderService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {
    @Provides
    @Singleton
    fun provideOrdersApiService(@TcomproApi retrofit: Retrofit): OrderService {
        return retrofit.create(OrderService::class.java)
    }
}

