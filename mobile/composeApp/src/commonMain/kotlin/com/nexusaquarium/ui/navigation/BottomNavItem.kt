package com.nexusaquarium.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    data object Home : BottomNavItem("home", "Home", Icons.Default.Home)
    data object Add : BottomNavItem("add", "Add", Icons.Default.Add)
    data object Profile : BottomNavItem("profile", "Profile", Icons.Default.Person)
    data object Settings : BottomNavItem("settings", "Settings", Icons.Default.Settings)
}

