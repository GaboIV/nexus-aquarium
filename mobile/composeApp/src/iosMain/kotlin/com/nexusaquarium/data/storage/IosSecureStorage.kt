package com.nexusaquarium.data.storage

import com.nexusaquarium.data.model.User
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import platform.Foundation.NSUserDefaults

class IosSecureStorage : SecureStorage {
    
    private val userDefaults = NSUserDefaults.standardUserDefaults
    private val json = Json { ignoreUnknownKeys = true }
    
    companion object {
        private const val TOKEN_KEY = "auth_token"
        private const val USER_KEY = "user_data"
    }
    
    override suspend fun saveToken(token: String) {
        userDefaults.setObject(token, TOKEN_KEY)
    }
    
    override suspend fun getToken(): String? {
        return userDefaults.stringForKey(TOKEN_KEY)
    }
    
    override suspend fun saveUser(user: User) {
        val userJson = json.encodeToString(user)
        userDefaults.setObject(userJson, USER_KEY)
    }
    
    override suspend fun getUser(): User? {
        val userJson = userDefaults.stringForKey(USER_KEY) ?: return null
        return try {
            json.decodeFromString<User>(userJson)
        } catch (e: Exception) {
            null
        }
    }
    
    override suspend fun clearAuth() {
        userDefaults.removeObjectForKey(TOKEN_KEY)
        userDefaults.removeObjectForKey(USER_KEY)
    }
    
    override suspend fun isLoggedIn(): Boolean {
        return getToken() != null && getUser() != null
    }
}
