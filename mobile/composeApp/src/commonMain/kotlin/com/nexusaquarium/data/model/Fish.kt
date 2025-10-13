package com.nexusaquarium.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Fish(
    val id: Int,
    val name: String,
    val scientificName: String,
    val imageUrl: String? = null
)
