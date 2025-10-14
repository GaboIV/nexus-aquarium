package com.nexusaquarium.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nexusaquarium.data.model.Aquarium
import com.nexusaquarium.ui.viewmodel.AquariumViewModel
import kotlin.math.roundToInt

@Composable
fun AddEditAquariumScreen(
    viewModel: AquariumViewModel,
    aquarium: Aquarium? = null,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isLoading = viewModel.isLoading
    val errorMessage = viewModel.errorMessage
    
    // Form state
    var name by remember { mutableStateOf(aquarium?.name ?: "") }
    var description by remember { mutableStateOf(aquarium?.description ?: "") }
    var lengthCm by remember { mutableStateOf(aquarium?.lengthCm?.toString() ?: "") }
    var heightCm by remember { mutableStateOf(aquarium?.heightCm?.toString() ?: "") }
    var depthCm by remember { mutableStateOf(aquarium?.depthCm?.toString() ?: "") }
    var aquariumType by remember { mutableStateOf(aquarium?.aquariumType ?: "freshwater") }
    var volumeReductionPercentage by remember { mutableStateOf(aquarium?.volumeReductionPercentage?.toString() ?: "15") }
    var hasHeater by remember { mutableStateOf(aquarium?.hasHeater ?: false) }
    var hasFilter by remember { mutableStateOf(aquarium?.hasFilter ?: false) }
    var hasLighting by remember { mutableStateOf(aquarium?.hasLighting ?: false) }
    var hasCo2 by remember { mutableStateOf(aquarium?.hasCo2 ?: false) }
    var hasAirPump by remember { mutableStateOf(aquarium?.hasAirPump ?: false) }
    var substrateType by remember { mutableStateOf(aquarium?.substrateType ?: "") }
    var substrateDepthCm by remember { mutableStateOf(aquarium?.substrateDepthCm?.toString() ?: "") }
    var hasPlants by remember { mutableStateOf(aquarium?.hasPlants ?: false) }
    var hasDecorations by remember { mutableStateOf(aquarium?.hasDecorations ?: false) }
    var decorationDescription by remember { mutableStateOf(aquarium?.decorationDescription ?: "") }
    var notes by remember { mutableStateOf(aquarium?.notes ?: "") }
    
    // Calculate volumes
    val totalVolume = remember(lengthCm, heightCm, depthCm) {
        try {
            val length = lengthCm.toDoubleOrNull() ?: 0.0
            val height = heightCm.toDoubleOrNull() ?: 0.0
            val depth = depthCm.toDoubleOrNull() ?: 0.0
            (length * height * depth) / 1000.0
        } catch (e: Exception) {
            0.0
        }
    }
    
    val realVolume = remember(totalVolume, volumeReductionPercentage) {
        try {
            val reduction = volumeReductionPercentage.toDoubleOrNull() ?: 15.0
            totalVolume * (1.0 - reduction / 100.0)
        } catch (e: Exception) {
            0.0
        }
    }
    
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
            TextButton(onClick = onBack) {
                Text("Cancelar")
            }
            Text(
                text = if (aquarium == null) "Nuevo Acuario" else "Editar Acuario",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Button(
                onClick = {
                    val newAquarium = Aquarium(
                        id = aquarium?.id ?: 0,
                        name = name,
                        description = description.ifBlank { null },
                        imageUrl = null,
                        imageGallery = null,
                        lengthCm = lengthCm.toDoubleOrNull() ?: 0.0,
                        heightCm = heightCm.toDoubleOrNull() ?: 0.0,
                        depthCm = depthCm.toDoubleOrNull() ?: 0.0,
                        totalVolumeLiters = totalVolume,
                        realVolumeLiters = realVolume,
                        volumeReductionPercentage = volumeReductionPercentage.toDoubleOrNull() ?: 15.0,
                        aquariumType = aquariumType,
                        setupDate = aquarium?.setupDate,
                        isActive = aquarium?.isActive ?: true,
                        currentTemperature = aquarium?.currentTemperature,
                        desiredTemperature = aquarium?.desiredTemperature,
                        currentPh = aquarium?.currentPh,
                        desiredPh = aquarium?.desiredPh,
                        currentGh = aquarium?.currentGh,
                        desiredGh = aquarium?.desiredGh,
                        hasHeater = hasHeater,
                        hasFilter = hasFilter,
                        hasLighting = hasLighting,
                        hasCo2 = hasCo2,
                        hasAirPump = hasAirPump,
                        substrateType = substrateType.ifBlank { null },
                        substrateDepthCm = substrateDepthCm.toDoubleOrNull(),
                        hasPlants = hasPlants,
                        hasDecorations = hasDecorations,
                        decorationDescription = decorationDescription.ifBlank { null },
                        lastWaterChange = aquarium?.lastWaterChange,
                        waterChangeFrequency = aquarium?.waterChangeFrequency,
                        lastCleaning = aquarium?.lastCleaning,
                        notes = notes.ifBlank { null },
                        observations = aquarium?.observations
                    )
                    
                    if (aquarium == null) {
                        viewModel.createAquarium(newAquarium)
                    } else {
                        viewModel.updateAquarium(aquarium.id, newAquarium)
                    }
                    onBack()
                },
                enabled = name.isNotBlank() && lengthCm.isNotBlank() && heightCm.isNotBlank() && depthCm.isNotBlank()
            ) {
                Text(if (aquarium == null) "Crear" else "Guardar")
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
        
        // Form
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            // Basic Information
            Text(
                text = "Información Básica",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre del acuario") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripción (opcional)") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Dimensions
            Text(
                text = "Dimensiones (cm)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = lengthCm,
                    onValueChange = { lengthCm = it },
                    label = { Text("Largo") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                OutlinedTextField(
                    value = heightCm,
                    onValueChange = { heightCm = it },
                    label = { Text("Alto") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                OutlinedTextField(
                    value = depthCm,
                    onValueChange = { depthCm = it },
                    label = { Text("Profundidad") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }
            
            // Volume calculations
            if (totalVolume > 0) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Cálculos de Volumen",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Volumen total: ${totalVolume.roundToInt()}L",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Volumen real: ${realVolume.roundToInt()}L",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Aquarium Type
            Text(
                text = "Tipo de Acuario",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            
            val aquariumTypes = listOf(
                "freshwater" to "🐟 Agua Dulce",
                "saltwater" to "🐠 Agua Salada",
                "brackish" to "🦐 Agua Salobre",
                "planted" to "🌱 Plantado",
                "reef" to "🪸 Arrecife"
            )
            
            aquariumTypes.forEach { (type, displayName) ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = aquariumType == type,
                        onClick = { aquariumType = type }
                    )
                    Text(
                        text = displayName,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Volume Reduction
            OutlinedTextField(
                value = volumeReductionPercentage,
                onValueChange = { volumeReductionPercentage = it },
                label = { Text("Reducción de volumen (%)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                suffix = { Text("%") }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Equipment
            Text(
                text = "Equipamiento",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            
            // Heater
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = hasHeater,
                    onCheckedChange = { hasHeater = it }
                )
                Text(
                    text = "🔥 Calentador",
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            
            // Filter
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = hasFilter,
                    onCheckedChange = { hasFilter = it }
                )
                Text(
                    text = "🔧 Filtro",
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            
            // Lighting
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = hasLighting,
                    onCheckedChange = { hasLighting = it }
                )
                Text(
                    text = "💡 Iluminación",
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            
            // CO2
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = hasCo2,
                    onCheckedChange = { hasCo2 = it }
                )
                Text(
                    text = "💨 CO2",
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            
            // Air Pump
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = hasAirPump,
                    onCheckedChange = { hasAirPump = it }
                )
                Text(
                    text = "💨 Bomba de aire",
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Substrate
            Text(
                text = "Sustrato y Decoraciones",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            
            val substrateTypes = listOf(
                "" to "Sin sustrato",
                "gravel" to "🪨 Grava",
                "sand" to "🏖️ Arena",
                "soil" to "🌱 Tierra",
                "bare_bottom" to "🔲 Fondo desnudo"
            )
            
            substrateTypes.forEach { (type, displayName) ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = substrateType == type,
                        onClick = { substrateType = type }
                    )
                    Text(
                        text = displayName,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
            
            if (substrateType.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = substrateDepthCm,
                    onValueChange = { substrateDepthCm = it },
                    label = { Text("Profundidad del sustrato (cm)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    suffix = { Text("cm") }
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = hasPlants,
                    onCheckedChange = { hasPlants = it }
                )
                Text(
                    text = "🌱 Plantas",
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = hasDecorations,
                    onCheckedChange = { hasDecorations = it }
                )
                Text(
                    text = "🎨 Decoraciones",
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            
            if (hasDecorations) {
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = decorationDescription,
                    onValueChange = { decorationDescription = it },
                    label = { Text("Descripción de decoraciones") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 2
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Notes
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Notas (opcional)") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3
            )
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
