package com.nexusaquarium.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.History
import androidx.compose.ui.graphics.vector.ImageVector

// Navigation items for MVP Stage 1: Digital Aquarium Diary
sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    data object Aquariums : BottomNavItem("aquariums", "Acuarios", Icons.Default.Home)
    data object Parameters : BottomNavItem("parameters", "Parámetros", Icons.Default.Science)
    data object Inhabitants : BottomNavItem("inhabitants", "Habitantes", Icons.Default.Pets)
    data object History : BottomNavItem("history", "Historial", Icons.Default.History)
}

