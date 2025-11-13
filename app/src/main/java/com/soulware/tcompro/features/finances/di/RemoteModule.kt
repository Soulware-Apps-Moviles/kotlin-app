package com.soulware.tcompro.features.finances.di

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
    @Named("base_url")
    fun provideBaseUrl(): String = "http://20.201.98.91:8080/"

    @Provides
    @Singleton
    fun provideRetrofit(@Named("base_url") url: String): Retrofit {
        val token =
            ""

        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
                chain.proceed(newRequest)
            }
            .build()

        return Retrofit.Builder()
            .baseUrl(url)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideFinanceApiService(retrofit: Retrofit): FinanceService {
        return retrofit.create(FinanceService::class.java)
    }
}
