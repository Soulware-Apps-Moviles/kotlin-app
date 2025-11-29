package com.soulware.tcompro.features.auth.data

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ProfileApiService {

    // --- CORRECCIÓN: AÑADIMOS EL HEADER DE AUTORIZACIÓN ---
    @POST("profile/v1")
    suspend fun createProfile(
        @Header("Authorization") token: String, // <--- Nuevo parámetro
        @Body request: CreateProfileRequest
    ): Unit

    @GET("owners/v1/")
    suspend fun getOwnerByEmail(
        @Header("Authorization") token: String,
        @Query("email") email: String
    ): OwnerResource
}

// Asegúrate de que CreateProfileRequest tenga el rol OWNER como hablamos
data class CreateProfileRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String,
    val authId: String,
    val role: String ="SHOP_OWNER"
)

data class OwnerResource(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String,
    val shopId: Long
)