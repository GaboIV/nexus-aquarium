package com.nexusaquarium.data.remote

import com.nexusaquarium.config.AppConfig
import com.nexusaquarium.data.model.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.http.*

class AuthApiService(private val httpClient: HttpClient) {
    
    private val baseUrl = AppConfig.API_BASE_URL
    
    // Register new user
    suspend fun register(request: RegisterRequest): AuthResponse {
        return httpClient.post("$baseUrl/api/v1/auth/register") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }
    
    // Login user
    suspend fun login(request: LoginRequest): AuthResponse {
        return httpClient.post("$baseUrl/api/v1/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }
    
    // Get user profile (requires authentication)
    suspend fun getUserProfile(token: String): UserProfile {
        return httpClient.get("$baseUrl/api/v1/users/me") {
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
        }.body()
    }
    
    // Update user profile (requires authentication)
    suspend fun updateUserProfile(token: String, displayName: String) {
        httpClient.put("$baseUrl/api/v1/users/me") {
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
            contentType(ContentType.Application.Json)
            setBody(mapOf("displayName" to displayName))
        }
    }
    
    // Register device for push notifications (requires authentication)
    suspend fun registerDevice(token: String, request: DeviceRegistrationRequest) {
        httpClient.post("$baseUrl/api/v1/users/me/devices") {
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
            contentType(ContentType.Application.Json)
            setBody(request)
        }
    }
    
    // Get user preferences (requires authentication)
    suspend fun getUserPreferences(token: String): UserPreferences {
        return httpClient.get("$baseUrl/api/v1/users/me/preferences") {
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
        }.body()
    }
    
    // Update user preferences (requires authentication)
    suspend fun updateUserPreferences(token: String, preferences: UserPreferences) {
        httpClient.put("$baseUrl/api/v1/users/me/preferences") {
            headers {
                append(HttpHeaders.Authorization, "Bearer $token")
            }
            contentType(ContentType.Application.Json)
            setBody(preferences)
        }
    }
}
