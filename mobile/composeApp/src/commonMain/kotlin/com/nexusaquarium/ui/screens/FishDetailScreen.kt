package com.nexusaquarium.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
    onBackClick: () -> Unit,
    onDeleteClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Top App Bar
            TopAppBar(
            title = {
                Text(
                    text = fish.commonName,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            },
                navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver"
                    )
                }
            },
            actions = {
                IconButton(onClick = { onDeleteClick(fish.id) }) {
                        Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = MaterialTheme.colorScheme.error
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface,
                titleContentColor = MaterialTheme.colorScheme.onSurface
            )
        )

        // Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Hero Image Section
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
                    
                    // Gradient overlay for better text visibility
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.surface.copy(alpha = 0f),
                                        MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)
                                    )
                                )
                            )
                    )
                } else {
                    // Placeholder when no image
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
            
                // Fish name overlay
            Column(
                modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(24.dp)
            ) {
                Text(
                    text = fish.commonName,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                        color = if (fish.imageUrl != null) 
                            MaterialTheme.colorScheme.onSurface 
                        else 
                            MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = fish.scientificName,
                    style = MaterialTheme.typography.titleMedium,
                    fontStyle = FontStyle.Italic,
                        color = if (fish.imageUrl != null) 
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                        else 
                            MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                    )
                }
            }

            // Content Cards
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Quick Info Card
                QuickInfoCard(fish)
                
                // Water Parameters Card
                WaterParametersCard(fish)
                
                // Behavior Card
                BehaviorCard(fish)
                
                // Overview Card
                OverviewCard(fish)
                
                // Diet Card
                DietCard(fish)
                
                // Warnings Card (if applicable)
                if (fish.isPredator || fish.isFinNipper) {
                    WarningsCard(fish)
                }
            }
        }
    }
}

@Composable
private fun QuickInfoCard(fish: Fish) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Información Rápida",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                InfoChip(
                    icon = "🌡️",
                    label = "Temperatura",
                    value = fish.getTemperatureRange()
                )
                InfoChip(
                    icon = "💧",
                    label = "pH",
                    value = fish.getPhRange()
                )
                InfoChip(
                    icon = "📏",
                    label = "Tamaño",
                    value = "${fish.maxSizeCm} cm"
                )
            }
        }
    }
}

@Composable
private fun WaterParametersCard(fish: Fish) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Parámetros del Agua",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            
            DetailRow(label = "Temperatura", value = fish.getTemperatureRange())
            DetailRow(label = "pH", value = fish.getPhRange())
            fish.ghMin?.let { ghMin ->
                fish.ghMax?.let { ghMax ->
                    DetailRow(label = "Dureza General (GH)", value = "$ghMin - $ghMax dGH")
                }
            }
            DetailRow(label = "Acuario mínimo", value = "${fish.minTankSizeLiters} litros")
        }
    }
}

@Composable
private fun BehaviorCard(fish: Fish) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Comportamiento",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = fish.getTemperamentEmoji(),
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = when (fish.temperament) {
                            "peaceful" -> "Pacífico"
                            "semi_aggressive" -> "Semi-agresivo"
                            "aggressive" -> "Agresivo"
                            else -> fish.temperament
                        },
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                }
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = fish.getSocialBehaviorEmoji(),
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = when (fish.socialBehavior) {
                            "schooling" -> "Cardumen"
                            "shoaling" -> "Grupo"
                            "pairs" -> "Pareja"
                            "solitary" -> "Solitario"
                            else -> fish.socialBehavior
                        } + (fish.minGroupSize?.let { " (min $it)" } ?: ""),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            
            DetailRow(
                label = "Nivel de nado",
                value = when(fish.tankLevel) {
                        "bottom" -> "Fondo"
                        "mid" -> "Medio"
                        "top" -> "Superficie"
                        "all" -> "Todos los niveles"
                        else -> fish.tankLevel
                }
            )
            
            DetailRow(
                label = "Dificultad",
                value = when(fish.difficultyLevel) {
                    "beginner" -> "Principiante"
                    "intermediate" -> "Intermedio"
                    "advanced" -> "Avanzado"
                    else -> fish.difficultyLevel
                } + " ${fish.getDifficultyStars()}"
            )
        }
    }
}

@Composable
private fun OverviewCard(fish: Fish) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
                        Text(
                text = "Visión General",
                style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
            
            DetailRow(label = "Origen", value = fish.origin)
            DetailRow(label = "Esperanza de vida", value = "${fish.lifeExpectancyYears} años")
            DetailRow(label = "Acuario mínimo", value = "${fish.minTankSizeLiters}L")
            DetailRow(label = "Nivel de nado", value = when(fish.tankLevel) {
                "bottom" -> "Fondo"
                "mid" -> "Medio"
                "top" -> "Superficie"
                "all" -> "Todos los niveles"
                else -> fish.tankLevel
            })
        }
    }
}

@Composable
private fun DietCard(fish: Fish) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Alimentación",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            
            DetailRow(
                label = "Tipo de dieta",
                value = when(fish.dietType) {
                    "omnivore" -> "Omnívoro"
                    "carnivore" -> "Carnívoro"
                    "herbivore" -> "Herbívoro"
                    else -> fish.dietType
                }
            )
            
            fish.recommendedFoods?.let { foods ->
                DetailRow(label = "Alimentos recomendados", value = foods)
            }
        }
    }
}

@Composable
private fun WarningsCard(fish: Fish) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "⚠️ Advertencias",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.error
            )
            
            if (fish.isPredator) {
                Text(
                    text = "• Depredador: Puede comer peces pequeños",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }
            if (fish.isFinNipper) {
                Text(
                    text = "• Muerde aletas: No compatible con peces de aletas largas",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
private fun InfoChip(
    icon: String,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            text = icon,
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun DetailRow(
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
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}