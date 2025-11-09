/*
 * AuthInterceptor (Interceptor de Autenticación)
 *
 * Esta clase es un Interceptor de OkHttp que se adjunta automáticamente
 * a las llamadas de Retrofit dirigidas a nuestro backend (TcomproApi).
 *
 * Su única función es interceptar cada llamada, leer el 'accessToken'
 * guardado en el SessionManager (nuestro "llavero") y añadirlo a la
 * cabecera "Authorization" como un "Bearer Token".
 *
 * Si no hay token guardado, deja que la llamada pase sin modificar.
 */
package com.soulware.tcompro.core.di

import com.soulware.tcompro.core.data.SessionManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val sessionManager: SessionManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking {
            sessionManager.userSessionFlow.first().accessToken
        }

        val newRequest = if (token != null) {
            chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            chain.request()
        }

        return chain.proceed(newRequest)
    }
}