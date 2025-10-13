package com.nexusaquarium.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

// Main navigation items for Nexus Aquarium
sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    data object Home : BottomNavItem("home", "Home", Icons.Default.Home)
    data object Fish : BottomNavItem("fish", "Peces", Icons.Default.Pets)
    data object MyAquariums : BottomNavItem("my_aquariums", "Mis Acuarios", Icons.Default.WaterDrop)
    data object MyAccount : BottomNavItem("my_account", "Mi Cuenta", Icons.Default.Person)
}

