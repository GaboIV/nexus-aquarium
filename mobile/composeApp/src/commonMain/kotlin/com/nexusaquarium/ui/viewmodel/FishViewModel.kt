package com.nexusaquarium.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexusaquarium.data.model.Fish
import com.nexusaquarium.data.remote.FishApiService
import com.nexusaquarium.data.remote.HttpClientProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class FishUiState {
    data object Loading : FishUiState()
    data class Success(val fish: List<Fish>) : FishUiState()
    data class Error(val message: String) : FishUiState()
}

class FishViewModel : ViewModel() {
    private val apiService = FishApiService(HttpClientProvider.client)
    
    private val _uiState = MutableStateFlow<FishUiState>(FishUiState.Loading)
    val uiState: StateFlow<FishUiState> = _uiState.asStateFlow()

    init {
        loadFish()
    }

    fun loadFish() {
        viewModelScope.launch {
            _uiState.value = FishUiState.Loading
            try {
                println("🔍 Intentando conectar a: ${HttpClientProvider.getFishEndpoint()}")
                println("🔍 URL base: ${HttpClientProvider.getBaseUrl()}")
                println("🔍 Endpoint: ${HttpClientProvider.getFishEndpoint()}")
                
                val result = apiService.getAllFish()
                result
                    .onSuccess { fishList ->
                        println("✅ Datos recibidos: ${fishList.size} peces")
                        println("✅ Primer pez: ${fishList.firstOrNull()?.commonName}")
                        _uiState.value = FishUiState.Success(fishList)
                    }
                    .onFailure { exception ->
                        println("❌ Error de conexión: ${exception.message}")
                        println("❌ Tipo de error: ${exception.javaClass.simpleName}")
                        println("❌ Stack trace: ${exception.stackTraceToString()}")
                        _uiState.value = FishUiState.Error(
                            exception.message ?: "Unknown error occurred"
                        )
                    }
            } catch (e: Exception) {
                println("❌ Excepción no manejada: ${e.message}")
                println("❌ Stack trace: ${e.stackTraceToString()}")
                _uiState.value = FishUiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun deleteFish(fishId: Int) {
        viewModelScope.launch {
            apiService.deleteFish(fishId)
                .onSuccess {
                    // Reload the fish list after deletion
                    loadFish()
                }
                .onFailure { exception ->
                    _uiState.value = FishUiState.Error(
                        "Failed to delete fish: ${exception.message}"
                    )
                }
        }
    }
}

