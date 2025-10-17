package com.nexusaquarium.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexusaquarium.data.model.*
import com.nexusaquarium.data.remote.AuthApiService
import com.nexusaquarium.data.storage.SecureStorage
import com.nexusaquarium.data.storage.createSecureStorage
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val authApiService = AuthApiService(com.nexusaquarium.data.remote.HttpClientProvider.client)
    private val secureStorage: SecureStorage = createSecureStorage()

    // Backing MutableState values
    private val _authState = mutableStateOf<AuthState>(AuthState.Loading)
    val authState: androidx.compose.runtime.State<AuthState> get() = _authState

    private val _isLoading = mutableStateOf(false)
    val isLoading: androidx.compose.runtime.State<Boolean> get() = _isLoading

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: androidx.compose.runtime.State<String?> get() = _errorMessage
    
    init {
        checkAuthState()
    }
    
    // Check if user is already logged in
    private fun checkAuthState() {
        viewModelScope.launch {
            try {
                if (secureStorage.isLoggedIn()) {
                    val user = secureStorage.getUser()
                    if (user != null) {
                        // Verify token is still valid by getting user profile
                        val profile = authApiService.getUserProfile(user.token)
                        _authState.value = AuthState.LoggedIn(profile)
                    } else {
                        _authState.value = AuthState.LoggedOut
                    }
                } else {
                    _authState.value = AuthState.LoggedOut
                }
            } catch (e: Exception) {
                // Token might be expired, clear auth
                secureStorage.clearAuth()
                _authState.value = AuthState.LoggedOut
            }
        }
    }
    
    // Register new user
    fun register(email: String, password: String, displayName: String? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            
            try {
                val request = RegisterRequest(email, password, displayName)
                val response = authApiService.register(request)
                
                // Get user profile with the token
                val profile = authApiService.getUserProfile(response.token)
                
                // Save user data securely
                val user = User(
                    id = profile.id,
                    email = profile.email,
                    displayName = profile.displayName,
                    token = response.token
                )
                secureStorage.saveUser(user)
                secureStorage.saveToken(response.token)

                _authState.value = AuthState.LoggedIn(profile)
            } catch (e: Exception) {
                val msg = e.message ?: "Registration failed"
                _errorMessage.value = msg
                _authState.value = AuthState.Error(msg)
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    // Login user
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            
            try {
                val request = LoginRequest(email, password)
                val response = authApiService.login(request)
                
                // Get user profile with the token
                val profile = authApiService.getUserProfile(response.token)
                
                // Save user data securely
                val user = User(
                    id = profile.id,
                    email = profile.email,
                    displayName = profile.displayName,
                    token = response.token
                )
                secureStorage.saveUser(user)
                secureStorage.saveToken(response.token)

                _authState.value = AuthState.LoggedIn(profile)
            } catch (e: Exception) {
                val msg = e.message ?: "Login failed"
                _errorMessage.value = msg
                _authState.value = AuthState.Error(msg)
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    // Logout user
    fun logout() {
        viewModelScope.launch {
            secureStorage.clearAuth()
            _authState.value = AuthState.LoggedOut
            _errorMessage.value = null
        }
    }
    
    // Clear error message
    fun clearError() {
        _errorMessage.value = null
        if (_authState.value is AuthState.Error) {
            _authState.value = AuthState.LoggedOut
        }
    }
    
    // Get current user
    fun getCurrentUser(): UserProfile? {
        return when (val state = _authState.value) {
            is AuthState.LoggedIn -> state.user
            else -> null
        }
    }
    
    // Check if user is logged in
    fun isLoggedIn(): Boolean {
        return _authState.value is AuthState.LoggedIn
    }
}
