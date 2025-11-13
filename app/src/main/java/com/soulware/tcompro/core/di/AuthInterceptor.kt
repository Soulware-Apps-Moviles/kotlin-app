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

    private val fallbackToken = "eyJhbGciOiJIUzI1NiIsImtpZCI6IjUrb2RXOVlaN1JIbGZONXkiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FwcXNmcmJiYmNhY3pwZXlkeG94LnN1cGFiYXNlLmNvL2F1dGgvdjEiLCJzdWIiOiIwNmZiMWM1Mi0wZDYxLTRlZWMtYTFlZS00MGQ4YmQ1NjZjNzQiLCJhdWQiOiJhdXRoZW50aWNhdGVkIiwiZXhwIjoxNzYzMDU1MTU0LCJpYXQiOjE3NjMwNTE1NTQsImVtYWlsIjoidGVzdDEwQGV4YW1wbGUuY29tIiwicGhvbmUiOiIiLCJhcHBfbWV0YWRhdGEiOnsicHJvdmlkZXIiOiJlbWFpbCIsInByb3ZpZGVycyI6WyJlbWFpbCJdfSwidXNlcl9tZXRhZGF0YSI6eyJlbWFpbCI6InRlc3QxMEBleGFtcGxlLmNvbSIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJwaG9uZV92ZXJpZmllZCI6ZmFsc2UsInN1YiI6IjA2ZmIxYzUyLTBkNjEtNGVlYy1hMWVlLTQwZDhiZDU2NmM3NCJ9LCJyb2xlIjoiYXV0aGVudGljYXRlZCIsImFhbCI6ImFhbDEiLCJhbXIiOlt7Im1ldGhvZCI6InBhc3N3b3JkIiwidGltZXN0YW1wIjoxNzYzMDUxNTU0fV0sInNlc3Npb25faWQiOiJlNzRiN2Y1My0yYzQzLTRhYjgtOTNmNS0yNDk1YjEwNmFjZWYiLCJpc19hbm9ueW1vdXMiOmZhbHNlfQ.IfLxXdARMmeOwTNryRFOiYgDlp7_JBEGV6_2rKGqenw"

    override fun intercept(chain: Interceptor.Chain): Response {
        val sessionToken = runBlocking {
            sessionManager.userSessionFlow.first().accessToken
        }

        val tokenToUse = sessionToken ?: fallbackToken

        val newRequest = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $tokenToUse")
            .build()

        return chain.proceed(newRequest)
    }
}
