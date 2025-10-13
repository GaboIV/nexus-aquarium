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
            apiService.getAllFish()
                .onSuccess { fishList ->
                    _uiState.value = FishUiState.Success(fishList)
                }
                .onFailure { exception ->
                    _uiState.value = FishUiState.Error(
                        exception.message ?: "Unknown error occurred"
                    )
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

