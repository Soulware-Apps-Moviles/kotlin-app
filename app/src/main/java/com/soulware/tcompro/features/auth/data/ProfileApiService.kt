/*
 * ProfileApiService (Servicio de API de Perfil)
 *
 * Define la interfaz de Retrofit para comunicarse con el backend Tcompro
 * (no con Supabase) para gestionar la lógica de negocio de perfiles y dueños.
 *
 * Funcionalidades:
 * - Define el endpoint 'createProfile' (POST) para el flujo de Registro (Paso 2).
 * - Define el endpoint 'getOwnerByEmail' (GET) para el flujo de Login (Paso 2),
 * que es crucial para obtener el 'shopId' del usuario.
 * - Define los modelos de datos (DTOs) para la petición (CreateProfileRequest)
 * y la respuesta (OwnerResource).
 */
package com.soulware.tcompro.features.auth.data

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ProfileApiService {

    @POST("api/v1/profiles")
    suspend fun createProfile(@Body request: CreateProfileRequest): Any

    @GET("owners/v1/")
    suspend fun getOwnerByEmail(
        @Header("Authorization") authorization: String,
        @Query("email") email: String
    ): OwnerResource
}

data class CreateProfileRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String,
    val authId: String
)

data class OwnerResource(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String,
    val shopId: Long
)