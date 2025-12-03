
package com.soulware.tcompro.features.auth.data

import com.google.gson.annotations.SerializedName
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("auth/v1/token?grant_type=password")
    suspend fun signIn(@Body request: AuthRequest): AuthResponse

    @POST("auth/v1/signup")
    suspend fun signUp(@Body request: AuthRequest): AuthResponse
}

data class AuthRequest(
    val email: String,
    val password: String
)

data class AuthResponse(
    @SerializedName("access_token")
    val accessToken: String?,

    @SerializedName("refresh_token")
    val refreshToken: String?,

    @SerializedName("user")
    val user: UserResponse?
)

data class UserResponse(
    @SerializedName("id")
    val id: String?,

    @SerializedName("email")
    val email: String?
)