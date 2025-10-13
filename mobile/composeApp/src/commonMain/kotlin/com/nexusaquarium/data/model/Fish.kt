package com.nexusaquarium.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Fish(
    val id: Int,
    
    // Basic Information
    val commonName: String,
    val scientificName: String,
    val imageUrl: String? = null,
    val imageGallery: List<String>? = null,
    
    // Water Parameters
    val tempMinC: Double,
    val tempMaxC: Double,
    val phMin: Double,
    val phMax: Double,
    val ghMin: Int? = null,  // General Hardness (optional)
    val ghMax: Int? = null,
    
    // Size and Tank Requirements
    val maxSizeCm: Double,
    val minTankSizeLiters: Int,
    
    // Behavior and Compatibility
    val temperament: String,  // 'peaceful', 'semi_aggressive', 'aggressive'
    val socialBehavior: String,  // 'schooling', 'shoaling', 'pairs', 'solitary'
    val minGroupSize: Int? = null,
    val tankLevel: String,  // 'bottom', 'mid', 'top', 'all'
    val isPredator: Boolean = false,
    val isFinNipper: Boolean = false,
    
    // Overview Information
    val origin: String,
    val lifeExpectancyYears: Int,
    val difficultyLevel: String,  // 'beginner', 'intermediate', 'advanced'
    
    // Tank Setup
    val tankSetupDescription: String? = null,
    
    // Detailed Behavior
    val behaviorDescription: String? = null,
    val idealTankMates: String? = null,
    
    // Diet
    val dietType: String,  // 'omnivore', 'carnivore', 'herbivore'
    val recommendedFoods: String? = null,
    
    // Breeding
    val sexualDimorphism: String? = null,
    val breedingDifficulty: String? = null,  // 'easy', 'moderate', 'difficult'
    val breedingMethod: String? = null,
    
    // Variants (for species with multiple color morphs)
    val hasVariants: Boolean = false,
    val variantsDescription: String? = null
) {
    // Helper functions for UI
    fun getTemperatureRange(): String = "$tempMinC - $tempMaxC°C"
    fun getPhRange(): String = "$phMin - $phMax"
    fun getTemperamentEmoji(): String = when (temperament) {
        "peaceful" -> "☮️"
        "semi_aggressive" -> "⚠️"
        "aggressive" -> "🛑"
        else -> "❓"
    }
    fun getSocialBehaviorEmoji(): String = when (socialBehavior) {
        "schooling", "shoaling" -> "🐠🐠"
        "pairs" -> "💑"
        "solitary" -> "👤"
        else -> "❓"
    }
    fun getDifficultyStars(): String = when (difficultyLevel) {
        "beginner" -> "⭐"
        "intermediate" -> "⭐⭐"
        "advanced" -> "⭐⭐⭐"
        else -> "❓"
    }
}
