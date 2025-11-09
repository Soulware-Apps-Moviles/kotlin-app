/*
 * NetworkModule (Módulo de Red - Hilt)
 *
 * Esta es la clase de configuración de red más importante de la app.
 * Utiliza Hilt (Inyección de Dependencias) para "enseñarle" a la app cómo
 * crear y proveer las instancias de Retrofit, OkHttp y los ApiServices.
 *
 * Funcionalidades:
 * - Define Calificadores (@TcomproApi, @SupabaseApi) para diferenciar entre
 * nuestro backend (Tcompro) y el backend de autenticación (Supabase).
 * - Define las URLs base y la API Key de Supabase.
 * - Crea dos Interceptores:
 * 1. SupabaseInterceptor: Añade la 'apiKey' y 'Content-Type' a las llamadas de Supabase.
 * 2. TcomproInterceptor: Añade el 'Authorization: Bearer <token>' (usando AuthInterceptor)
 * a las llamadas de nuestro backend.
 * - Crea dos Clientes OkHttp: Cada uno configurado con su interceptor respectivo.
 * - Crea dos Instancias de Retrofit: Cada una configurada con su cliente OkHttp y URL base.
 * - Provee los ApiServices (ShopApiService, AuthApiService, ProfileApiService)
 * listos para ser inyectados en los Repositorios, usando la instancia de Retrofit correcta.
 */
package com.soulware.tcompro.core.di

import com.soulware.tcompro.core.data.SessionManager
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

    @Provides
    @Singleton
    fun provideProfileApiService(@TcomproApi retrofit: Retrofit): ProfileApiService {
        return retrofit.create(ProfileApiService::class.java)
    }
}