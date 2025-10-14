package com.nexusaquarium.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nexusaquarium.data.model.Aquarium
import com.nexusaquarium.ui.components.AquariumCard
import com.nexusaquarium.ui.viewmodel.AquariumViewModel

@Composable
fun AquariumsListScreen(
    viewModel: AquariumViewModel,
    onAquariumClick: (Aquarium) -> Unit,
    onAddAquarium: () -> Unit,
    modifier: Modifier = Modifier
) {
    val aquariums = viewModel.aquariums
    val isLoading = viewModel.isLoading
    val errorMessage = viewModel.errorMessage
    
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Mis Acuarios",
                style = MaterialTheme.typography.headlineMedium
            )
            FloatingActionButton(
                onClick = onAddAquarium,
                modifier = Modifier.size(48.dp)
            ) {
                Text("+", style = MaterialTheme.typography.titleLarge)
            }
        }
        
        // Error message
        if (errorMessage != null) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = errorMessage,
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        
        // Content
        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            aquariums.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "🐟",
                            style = MaterialTheme.typography.displayLarge
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No tienes acuarios aún",
                            style = MaterialTheme.typography.headlineSmall,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Crea tu primer acuario para comenzar",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = onAddAquarium
                        ) {
                            Text("Crear Acuario")
                        }
                    }
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(aquariums.size) { index ->
                        val aquarium = aquariums[index]
                        AquariumCard(
                            aquarium = aquarium,
                            onClick = { onAquariumClick(aquarium) }
                        )
                    }
                }
            }
        }
    }
}
