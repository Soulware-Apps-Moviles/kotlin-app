package com.soulware.tcompro.core.di

import com.soulware.tcompro.core.data.SessionManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request // Importado
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val sessionManager: SessionManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val originalRequest: Request = chain.request()
        val requestBuilder: Request.Builder = originalRequest.newBuilder()

        // --- INICIO DE LA CORRECCIÓN 1 (Arregla Bug 2) ---
        // Añadimos el Content-Type que faltaba (el bug de Swagger)
        requestBuilder.addHeader("Content-Type", "application/json")
        // --- FIN DE LA CORRECCIÓN 1 ---

        // --- INICIO DE LA CORRECCIÓN 2 (Arregla Bug 1) ---
        // Verificamos si la cabecera YA fue añadida manualmente (desde AuthRepository)
        if (originalRequest.header("Authorization") == null) {
            // Si no fue añadida, la leemos de la sesión
            val token = runBlocking {
                sessionManager.userSessionFlow.first().accessToken
            }
            if (token != null) {
                requestBuilder.addHeader("Authorization", "Bearer $token")
            }
        }
        // --- FIN DE LA CORRECCIÓN 2 ---

        return chain.proceed(requestBuilder.build())
    }
}