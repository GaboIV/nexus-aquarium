package com.nexusaquarium

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.nexusaquarium.ui.navigation.BottomNavigationBar
import com.nexusaquarium.ui.screens.AddFishScreen
import com.nexusaquarium.ui.screens.HomeScreen
import com.nexusaquarium.ui.screens.ProfileScreen
import com.nexusaquarium.ui.screens.SettingsScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        var currentRoute by remember { mutableStateOf("home") }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                BottomNavigationBar(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        currentRoute = route
                    }
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (currentRoute) {
                    "home" -> HomeScreen()
                    "add" -> AddFishScreen()
                    "profile" -> ProfileScreen()
                    "settings" -> SettingsScreen()
                }
            }
        }
    }
}