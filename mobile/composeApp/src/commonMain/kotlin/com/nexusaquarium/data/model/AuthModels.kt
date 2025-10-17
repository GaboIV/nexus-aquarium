package com.nexusaquarium.data.model

import kotlinx.serialization.Serializable

// User registration request
@Serializable
data class RegisterRequest(
    val email: String,
    val password: String,
    val displayName: String? = null
)

// User login request
@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

// User profile response
@Serializable
data class UserProfile(
    val id: Int,
    val email: String,
    val displayName: String?
)

// Auth response with JWT token
@Serializable
data class AuthResponse(
    val token: String
)

// Device registration request
@Serializable
data class DeviceRegistrationRequest(
    val deviceToken: String,
    val deviceOs: String
)

// User preferences
@Serializable
data class UserPreferences(
    val enableTaskReminders: Boolean = true,
    val enableParameterAlerts: Boolean = true
)

// Auth state
sealed class AuthState {
    object Loading : AuthState()
    object LoggedOut : AuthState()
    data class LoggedIn(val user: UserProfile) : AuthState()
    data class Error(val message: String) : AuthState()
}

// User data class for internal use
data class User(
    val id: Int,
    val email: String,
    val displayName: String?,
    val token: String
)
