package com.nexusaquarium.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.nexusaquarium.data.model.Fish

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FishDetailScreen(
    fish: Fish,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(fish.commonName) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
        ) {
            // Hero Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) {
                if (fish.imageUrl != null) {
                    AsyncImage(
                        model = fish.imageUrl,
                        contentDescription = fish.commonName,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.primaryContainer,
                                        MaterialTheme.colorScheme.secondaryContainer
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Image,
                            contentDescription = null,
                            modifier = Modifier.size(80.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                        )
                    }
                }
            }
            
            // Content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Header Section
                Text(
                    text = fish.commonName,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = fish.scientificName,
                    style = MaterialTheme.typography.titleMedium,
                    fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Quick Stats Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    QuickStatCard(
                        icon = fish.getTemperamentEmoji(),
                        label = when (fish.temperament) {
                            "peaceful" -> "Pacífico"
                            "semi_aggressive" -> "Semi-agresivo"
                            "aggressive" -> "Agresivo"
                            else -> fish.temperament
                        }
                    )
                    QuickStatCard(
                        icon = fish.getSocialBehaviorEmoji(),
                        label = when (fish.socialBehavior) {
                            "schooling" -> "Cardumen"
                            "shoaling" -> "Grupo"
                            "pairs" -> "Pareja"
                            "solitary" -> "Solitario"
                            else -> fish.socialBehavior
                        }
                    )
                    QuickStatCard(
                        icon = fish.getDifficultyStars(),
                        label = when (fish.difficultyLevel) {
                            "beginner" -> "Principiante"
                            "intermediate" -> "Intermedio"
                            "advanced" -> "Avanzado"
                            else -> fish.difficultyLevel
                        }
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Warnings
                if (fish.isPredator || fish.isFinNipper) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "⚠️ Advertencias",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            if (fish.isPredator) {
                                Text(
                                    text = "• Depredador: Puede comer peces pequeños",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onErrorContainer
                                )
                            }
                            if (fish.isFinNipper) {
                                Text(
                                    text = "• Muerde aletas: No compatible con peces de aletas largas",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onErrorContainer
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
                
                // Visión General
                SectionCard(title = "Visión General") {
                    InfoRow(label = "Origen", value = fish.origin)
                    InfoRow(label = "Esperanza de vida", value = "${fish.lifeExpectancyYears} años")
                    InfoRow(label = "Tamaño máximo", value = "${fish.maxSizeCm} cm")
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Parámetros del Agua
                SectionCard(title = "Parámetros del Agua") {
                    InfoRow(label = "Temperatura", value = fish.getTemperatureRange())
                    InfoRow(label = "pH", value = fish.getPhRange())
                    if (fish.ghMin != null && fish.ghMax != null) {
                        InfoRow(label = "Dureza (GH)", value = "${fish.ghMin} - ${fish.ghMax} dGH")
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Requisitos del Acuario
                SectionCard(title = "Requisitos del Acuario") {
                    InfoRow(label = "Tamaño mínimo", value = "${fish.minTankSizeLiters}L")
                    InfoRow(label = "Nivel de nado", value = when(fish.tankLevel) {
                        "bottom" -> "Fondo"
                        "mid" -> "Medio"
                        "top" -> "Superficie"
                        "all" -> "Todos los niveles"
                        else -> fish.tankLevel
                    })
                    if (fish.minGroupSize != null) {
                        InfoRow(label = "Tamaño mínimo del grupo", value = "${fish.minGroupSize} individuos")
                    }
                    
                    if (fish.tankSetupDescription != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = fish.tankSetupDescription,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Comportamiento y Compatibilidad
                SectionCard(title = "Comportamiento y Compatibilidad") {
                    if (fish.behaviorDescription != null) {
                        Text(
                            text = fish.behaviorDescription,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    
                    if (fish.idealTankMates != null) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Compañeros ideales:",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = fish.idealTankMates,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Alimentación
                SectionCard(title = "Alimentación") {
                    InfoRow(label = "Tipo de dieta", value = when(fish.dietType) {
                        "omnivore" -> "Omnívoro"
                        "carnivore" -> "Carnívoro"
                        "herbivore" -> "Herbívoro"
                        else -> fish.dietType
                    })
                    
                    if (fish.recommendedFoods != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = fish.recommendedFoods,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Reproducción
                SectionCard(title = "Reproducción") {
                    if (fish.breedingDifficulty != null) {
                        InfoRow(label = "Dificultad de cría", value = when(fish.breedingDifficulty) {
                            "easy" -> "Fácil"
                            "moderate" -> "Moderada"
                            "difficult" -> "Difícil"
                            else -> fish.breedingDifficulty
                        })
                    }
                    
                    if (fish.sexualDimorphism != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Dimorfismo sexual:",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = fish.sexualDimorphism,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    
                    if (fish.breedingMethod != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Método de reproducción:",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = fish.breedingMethod,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                
                // Variants Section
                if (fish.hasVariants && fish.variantsDescription != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "🎨 Variantes",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = fish.variantsDescription,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun QuickStatCard(
    icon: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = icon,
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
private fun SectionCard(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
    }
}

