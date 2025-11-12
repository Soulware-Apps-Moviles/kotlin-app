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
            "eyJhbGciOiJIUzI1NiIsImtpZCI6IjUrb2RXOVlaN1JIbGZONXkiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FwcXNmcmJiYmNhY3pwZXlkeG94LnN1cGFiYXNlLmNvL2F1dGgvdjEiLCJzdWIiOiIwNmZiMWM1Mi0wZDYxLTRlZWMtYTFlZS00MGQ4YmQ1NjZjNzQiLCJhdWQiOiJhdXRoZW50aWNhdGVkIiwiZXhwIjoxNzYyOTkyMTk2LCJpYXQiOjE3NjI5ODg1OTYsImVtYWlsIjoidGVzdDEwQGV4YW1wbGUuY29tIiwicGhvbmUiOiIiLCJhcHBfbWV0YWRhdGEiOnsicHJvdmlkZXIiOiJlbWFpbCIsInByb3ZpZGVycyI6WyJlbWFpbCJdfSwidXNlcl9tZXRhZGF0YSI6eyJlbWFpbCI6InRlc3QxMEBleGFtcGxlLmNvbSIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJwaG9uZV92ZXJpZmllZCI6ZmFsc2UsInN1YiI6IjA2ZmIxYzUyLTBkNjEtNGVlYy1hMWVlLTQwZDhiZDU2NmM3NCJ9LCJyb2xlIjoiYXV0aGVudGljYXRlZCIsImFhbCI6ImFhbDEiLCJhbXIiOlt7Im1ldGhvZCI6InBhc3N3b3JkIiwidGltZXN0YW1wIjoxNzYyOTg4NTk2fV0sInNlc3Npb25faWQiOiI4MTJjMmI3Yy1lZmFlLTQ5ODUtYmY1Ny0xYWRmNzZkYjUzMzIiLCJpc19hbm9ueW1vdXMiOmZhbHNlfQ.ZQxMyJBLE5KXdOtBJWW0WJFvjrujkT2a48QF_cMSMtM"

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
