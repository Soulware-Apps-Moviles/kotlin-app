package com.soulware.tcompro.core.di

import com.soulware.tcompro.features.auth.data.AuthApiService
import com.soulware.tcompro.features.auth.data.ProfileApiService
import com.soulware.tcompro.features.shop.data.ShopApiService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class TcomproApi

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SupabaseApi

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SupabaseInterceptor

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class TcomproInterceptor

// --- NUEVO: Calificador para el cliente de Login ---
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class TcomproApiLogin

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val TCOMPRO_BASE_URL = "http://20.201.98.91:8080/"
    private const val SUPABASE_BASE_URL = "https://apqsfrbbbcaczpeydxox.supabase.co/"
    private const val SUPABASE_API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImFwcXNmcmJiYmNhY3pwZXlkeG94Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTgyMzgzMzcsImV4cCI6MjA3MzgxNDMzN30.JykbEb3Co_YGLFNwOp7VeMlbI9Z24v5v0ZKWdnjqyFM"

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }

    @Provides
    @Singleton
    @SupabaseInterceptor
    fun provideSupabaseAuthInterceptor(): Interceptor {
        return Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("apiKey", SUPABASE_API_KEY)
                .addHeader("Content-Type", "application/json")
                .build()
            chain.proceed(request)
        }
    }

    @Provides
    @Singleton
    @TcomproInterceptor
    fun provideTcomproAuthInterceptor(authInterceptor: AuthInterceptor): Interceptor {
        return authInterceptor
    }

    // 1. CLIENTE NORMAL (Con Interceptor) -> Para Shop, Productos, etc.
    @Provides
    @Singleton
    @TcomproApi
    fun provideTcomproOkHttpClient(
        @TcomproInterceptor authInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()
    }

    // 2. CLIENTE LOGIN (Sin Interceptor) -> Solo para AuthRepository
    @Provides
    @Singleton
    @TcomproApiLogin
    fun provideTcomproLoginOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .build()
    }

    @Provides
    @Singleton
    @SupabaseApi
    fun provideSupabaseOkHttpClient(
        @SupabaseInterceptor interceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }

    // Retrofit Normal
    @Provides
    @Singleton
    @TcomproApi
    fun provideTcomproRetrofit(
        gson: Gson,
        @TcomproApi okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(TCOMPRO_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    // Retrofit Login (Nuevo)
    @Provides
    @Singleton
    @TcomproApiLogin
    fun provideTcomproLoginRetrofit(
        gson: Gson,
        @TcomproApiLogin okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(TCOMPRO_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    @SupabaseApi
    fun provideSupabaseRetrofit(
        gson: Gson,
        @SupabaseApi okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(SUPABASE_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    // ShopApi usa el cliente NORMAL
    @Provides
    @Singleton
    fun provideShopApiService(@TcomproApi retrofit: Retrofit): ShopApiService {
        return retrofit.create(ShopApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthApiService(@SupabaseApi retrofit: Retrofit): AuthApiService {
        return retrofit.create(AuthApiService::class.java)
    }

    // ProfileApiService usa el cliente LOGIN (¡IMPORTANTE!)
    @Provides
    @Singleton
    fun provideProfileApiService(@TcomproApiLogin retrofit: Retrofit): ProfileApiService {
        return retrofit.create(ProfileApiService::class.java)
    }
}