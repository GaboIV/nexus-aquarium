package com.nexusaquarium.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Aquarium(
    val id: Int,
    
    // Basic Information
    val name: String,
    val description: String? = null,
    val imageUrl: String? = null,
    val imageGallery: List<String>? = null,
    
    // Dimensions (in centimeters)
    val lengthCm: Double,
    val heightCm: Double,
    val depthCm: Double,
    
    // Calculated volumes
    val totalVolumeLiters: Double, // Calculated from dimensions
    val realVolumeLiters: Double,  // Real volume considering substrate, decorations, etc.
    val volumeReductionPercentage: Double = 15.0, // Default 15% reduction for substrate/decorations
    
    // Aquarium Type and Setup
    val aquariumType: String, // 'freshwater', 'saltwater', 'brackish', 'planted', 'reef'
    val setupDate: String? = null, // ISO date string
    val isActive: Boolean = true,
    
    // Water Parameters (current/desired)
    val currentTemperature: Double? = null,
    val desiredTemperature: Double? = null,
    val currentPh: Double? = null,
    val desiredPh: Double? = null,
    val currentGh: Int? = null,
    val desiredGh: Int? = null,
    
    // Equipment
    val hasHeater: Boolean = false,
    val hasFilter: Boolean = false,
    val hasLighting: Boolean = false,
    val hasCo2: Boolean = false,
    val hasAirPump: Boolean = false,
    
    // Substrate and Decorations
    val substrateType: String? = null, // 'gravel', 'sand', 'soil', 'bare_bottom'
    val substrateDepthCm: Double? = null,
    val hasPlants: Boolean = false,
    val hasDecorations: Boolean = false,
    val decorationDescription: String? = null,
    
    // Maintenance
    val lastWaterChange: String? = null, // ISO date string
    val waterChangeFrequency: Int? = null, // days between changes
    val lastCleaning: String? = null, // ISO date string
    
    // Notes and Observations
    val notes: String? = null,
    val observations: String? = null
) {
    // Helper functions for UI
    fun getDimensionsString(): String = "${lengthCm.toInt()} x ${heightCm.toInt()} x ${depthCm.toInt()} cm"
    
    fun getVolumeString(): String = "${totalVolumeLiters.toInt()}L total / ${realVolumeLiters.toInt()}L real"
    
    fun getVolumeReductionString(): String = "${volumeReductionPercentage.toInt()}% reducción"
    
    fun getAquariumTypeEmoji(): String = when (aquariumType) {
        "freshwater" -> "🐟"
        "saltwater" -> "🐠"
        "brackish" -> "🦐"
        "planted" -> "🌱"
        "reef" -> "🪸"
        else -> "🐡"
    }
    
    fun getSubstrateEmoji(): String = when (substrateType) {
        "gravel" -> "🪨"
        "sand" -> "🏖️"
        "soil" -> "🌱"
        "bare_bottom" -> "🔲"
        else -> "❓"
    }
    
    fun getEquipmentString(): String {
        val equipment = mutableListOf<String>()
        if (hasHeater) equipment.add("🔥")
        if (hasFilter) equipment.add("🔧")
        if (hasLighting) equipment.add("💡")
        if (hasCo2) equipment.add("💨")
        if (hasAirPump) equipment.add("💨")
        return equipment.joinToString(" ")
    }
    
    fun getStatusString(): String = if (isActive) "Activo" else "Inactivo"
    
    fun getStatusEmoji(): String = if (isActive) "✅" else "⏸️"
    
    // Calculate total volume from dimensions
    fun calculateTotalVolume(): Double {
        return (lengthCm * heightCm * depthCm) / 1000.0 // Convert cm³ to liters
    }
    
    // Calculate real volume considering substrate and decorations
    fun calculateRealVolume(): Double {
        return totalVolumeLiters * (1.0 - volumeReductionPercentage / 100.0)
    }
    
    // Check if maintenance is needed
    fun needsWaterChange(): Boolean {
        if (lastWaterChange == null || waterChangeFrequency == null) return false
        // This would need proper date parsing in a real implementation
        return true // Simplified for now
    }
    
    // Get aquarium size category
    fun getSizeCategory(): String = when {
        totalVolumeLiters < 50 -> "Nano"
        totalVolumeLiters < 100 -> "Pequeño"
        totalVolumeLiters < 200 -> "Mediano"
        totalVolumeLiters < 500 -> "Grande"
        else -> "Muy Grande"
    }
    
    fun getSizeCategoryEmoji(): String = when (getSizeCategory()) {
        "Nano" -> "🔬"
        "Pequeño" -> "📦"
        "Mediano" -> "📦📦"
        "Grande" -> "📦📦📦"
        "Muy Grande" -> "🏢"
        else -> "❓"
    }
}
