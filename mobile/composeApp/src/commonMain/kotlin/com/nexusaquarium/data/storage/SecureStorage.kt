package com.nexusaquarium.data.storage

// Common interface for secure storage across platforms
interface SecureStorage {
    suspend fun saveToken(token: String)
    suspend fun getToken(): String?
    suspend fun saveUser(user: com.nexusaquarium.data.model.User)
    suspend fun getUser(): com.nexusaquarium.data.model.User?
    suspend fun clearAuth()
    suspend fun isLoggedIn(): Boolean
}
