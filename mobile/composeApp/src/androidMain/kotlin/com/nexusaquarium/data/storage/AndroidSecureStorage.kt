package com.nexusaquarium.data.storage

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.nexusaquarium.data.model.User
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class AndroidSecureStorage(private val context: Context) : SecureStorage {
    
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()
    
    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "nexus_aquarium_secure_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
    
    private val json = Json { ignoreUnknownKeys = true }
    
    companion object {
        private const val TOKEN_KEY = "auth_token"
        private const val USER_KEY = "user_data"
    }
    
    override suspend fun saveToken(token: String) {
        sharedPreferences.edit()
            .putString(TOKEN_KEY, token)
            .apply()
    }
    
    override suspend fun getToken(): String? {
        return sharedPreferences.getString(TOKEN_KEY, null)
    }
    
    override suspend fun saveUser(user: User) {
        val userJson = json.encodeToString(user)
        sharedPreferences.edit()
            .putString(USER_KEY, userJson)
            .apply()
    }
    
    override suspend fun getUser(): User? {
        val userJson = sharedPreferences.getString(USER_KEY, null) ?: return null
        return try {
            json.decodeFromString<User>(userJson)
        } catch (e: Exception) {
            null
        }
    }
    
    override suspend fun clearAuth() {
        sharedPreferences.edit()
            .remove(TOKEN_KEY)
            .remove(USER_KEY)
            .apply()
    }
    
    override suspend fun isLoggedIn(): Boolean {
        return getToken() != null && getUser() != null
    }
}
