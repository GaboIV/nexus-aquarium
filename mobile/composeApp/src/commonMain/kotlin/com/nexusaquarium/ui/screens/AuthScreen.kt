package com.nexusaquarium.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nexusaquarium.ui.viewmodel.AuthViewModel

@Composable
fun AuthScreen(
    authViewModel: AuthViewModel,
    onAuthSuccess: () -> Unit
) {
    var isLoginMode by remember { mutableStateOf(true) }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surface,
                        MaterialTheme.colorScheme.surfaceContainerLowest
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        if (isLoginMode) {
            LoginScreen(
                authViewModel = authViewModel,
                onNavigateToRegister = { isLoginMode = false },
                onLoginSuccess = onAuthSuccess
            )
        } else {
            RegisterScreen(
                authViewModel = authViewModel,
                onNavigateToLogin = { isLoginMode = true },
                onRegisterSuccess = onAuthSuccess
            )
        }
    }
}
