package com.nexusaquarium.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.nexusaquarium.ui.viewmodel.AuthViewModel

@Composable
fun AuthDialog(
    authViewModel: AuthViewModel,
    onDismiss: () -> Unit,
    onAuthSuccess: () -> Unit
) {
    var isLoginMode by remember { mutableStateOf(true) }
    
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.surface,
                                MaterialTheme.colorScheme.surfaceContainerLowest
                            )
                        )
                    )
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
    }
}
