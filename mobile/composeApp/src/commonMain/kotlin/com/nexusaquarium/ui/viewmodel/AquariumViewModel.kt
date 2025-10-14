package com.nexusaquarium.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexusaquarium.data.model.Aquarium
import com.nexusaquarium.data.remote.AquariumApiService
import kotlinx.coroutines.launch

class AquariumViewModel(private val aquariumApiService: AquariumApiService) : ViewModel() {
    
    var aquariums by mutableStateOf<List<Aquarium>>(emptyList())
        private set
    
    var isLoading by mutableStateOf(false)
        private set
    
    var errorMessage by mutableStateOf<String?>(null)
        private set
    
    var selectedAquarium by mutableStateOf<Aquarium?>(null)
        private set
    
    init {
        loadAquariums()
    }
    
    fun loadAquariums() {
        viewModelScope.launch {
            try {
                isLoading = true
                errorMessage = null
                aquariums = aquariumApiService.getAllAquariums()
            } catch (e: Exception) {
                errorMessage = "Error al cargar acuarios: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
    
    fun getAquariumById(id: Int) {
        viewModelScope.launch {
            try {
                isLoading = true
                errorMessage = null
                selectedAquarium = aquariumApiService.getAquariumById(id)
            } catch (e: Exception) {
                errorMessage = "Error al cargar acuario: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
    
    fun createAquarium(aquarium: Aquarium) {
        viewModelScope.launch {
            try {
                isLoading = true
                errorMessage = null
                val newId = aquariumApiService.createAquarium(aquarium)
                loadAquariums() // Reload the list
            } catch (e: Exception) {
                errorMessage = "Error al crear acuario: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
    
    fun updateAquarium(id: Int, aquarium: Aquarium) {
        viewModelScope.launch {
            try {
                isLoading = true
                errorMessage = null
                aquariumApiService.updateAquarium(id, aquarium)
                loadAquariums() // Reload the list
                if (selectedAquarium?.id == id) {
                    selectedAquarium = aquarium.copy(id = id)
                }
            } catch (e: Exception) {
                errorMessage = "Error al actualizar acuario: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
    
    fun deleteAquarium(id: Int) {
        viewModelScope.launch {
            try {
                isLoading = true
                errorMessage = null
                aquariumApiService.deleteAquarium(id)
                loadAquariums() // Reload the list
                if (selectedAquarium?.id == id) {
                    selectedAquarium = null
                }
            } catch (e: Exception) {
                errorMessage = "Error al eliminar acuario: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
    
    fun clearError() {
        errorMessage = null
    }
    
    fun clearSelection() {
        selectedAquarium = null
    }
}
