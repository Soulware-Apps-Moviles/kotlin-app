package com.soulware.tcompro.features.finances.di

import com.soulware.tcompro.core.di.TcomproApi
import com.soulware.tcompro.features.finances.data.remote.services.FinanceService
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
    fun provideFinanceApiService(@TcomproApi retrofit: Retrofit): FinanceService {
        return retrofit.create(FinanceService::class.java)
    }
}
