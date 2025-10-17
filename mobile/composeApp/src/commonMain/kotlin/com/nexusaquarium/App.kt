package com.nexusaquarium

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Scaffold
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nexusaquarium.data.model.Aquarium
import com.nexusaquarium.data.model.Fish
import com.nexusaquarium.data.remote.AquariumApiService
import com.nexusaquarium.data.remote.HttpClientProvider
import com.nexusaquarium.ui.navigation.BottomNavigationBar
import com.nexusaquarium.ui.screens.NewHomeScreen
import com.nexusaquarium.ui.screens.AuthScreen
import com.nexusaquarium.ui.screens.ProfileScreen
import com.nexusaquarium.ui.screens.HomeTopAppBar
import com.nexusaquarium.ui.screens.AuthDialog
import com.nexusaquarium.ui.screens.FishScreen
import com.nexusaquarium.ui.screens.FishTopAppBar
import com.nexusaquarium.ui.screens.FishDetailScreen
import com.nexusaquarium.ui.screens.MyAquariumsScreen
import com.nexusaquarium.ui.screens.MyAquariumsTopAppBar
import com.nexusaquarium.ui.screens.MyAccountScreen
import com.nexusaquarium.ui.screens.AquariumDetailScreen
import com.nexusaquarium.ui.screens.AddEditAquariumScreen
import com.nexusaquarium.ui.theme.AppTheme
import com.nexusaquarium.ui.theme.ThemeViewModel
import com.nexusaquarium.ui.viewmodel.AquariumViewModel
import com.nexusaquarium.ui.viewmodel.FishViewModel
import com.nexusaquarium.ui.viewmodel.AuthViewModel
import com.nexusaquarium.data.model.AuthState
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    val themeViewModel: ThemeViewModel = viewModel()
    val themeMode by themeViewModel.themeMode.collectAsState()
    
    // Initialize services
    val httpClient = HttpClientProvider.client
    val aquariumApiService = remember { AquariumApiService(httpClient) }
    val aquariumViewModel: AquariumViewModel = viewModel { AquariumViewModel(aquariumApiService) }
    val fishViewModel: FishViewModel = viewModel { FishViewModel() }
    val authViewModel: AuthViewModel = viewModel { AuthViewModel() }
    
    AppTheme(themeMode = themeMode) {
        // Observe ViewModel State<T> properties
        val authState by authViewModel.authState
        
        var currentRoute by remember { mutableStateOf("home") }
        var selectedAquarium by remember { mutableStateOf<Aquarium?>(null) }
        var selectedFish by remember { mutableStateOf<Fish?>(null) }
        var isAddingAquarium by remember { mutableStateOf(false) }
        var isEditingAquarium by remember { mutableStateOf(false) }
        var showAuthDialog by remember { mutableStateOf(false) }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                when (currentRoute) {
                    "fish" -> FishTopAppBar(
                        onRefreshClick = { fishViewModel.loadFish() }
                    )
                    "my_aquariums" -> MyAquariumsTopAppBar(
                        authViewModel = authViewModel,
                        onLoginClick = { showAuthDialog = true }
                    )
                    "home" -> HomeTopAppBar(
                        authViewModel = authViewModel,
                        onLoginClick = { showAuthDialog = true }
                    )
                    // Add other topBars for different routes as needed
                }
            },
            bottomBar = {
                if (currentRoute != "aquarium_detail" && currentRoute != "add_aquarium" && currentRoute != "edit_aquarium" && currentRoute != "fish_detail") {
                    BottomNavigationBar(
                        currentRoute = currentRoute,
                        onNavigate = { route ->
                            currentRoute = route
                        }
                    )
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (currentRoute) {
                    "home" -> NewHomeScreen(authViewModel = authViewModel)
                    "fish" -> FishScreen(
                        paddingValues = PaddingValues(0.dp),
                        viewModel = fishViewModel,
                        onFishClick = { fish ->
                            selectedFish = fish
                            currentRoute = "fish_detail"
                        }
                    )
                    "my_aquariums" -> MyAquariumsScreen(
                        paddingValues = PaddingValues(0.dp),
                        viewModel = aquariumViewModel,
                        onAquariumClick = { aquarium ->
                            selectedAquarium = aquarium
                            currentRoute = "aquarium_detail"
                        },
                        onAddAquarium = {
                            isAddingAquarium = true
                            currentRoute = "add_aquarium"
                        },
                        authViewModel = authViewModel,
                        onLoginClick = { showAuthDialog = true }
                    )
                    "aquarium_detail" -> selectedAquarium?.let { aquarium ->
                        AquariumDetailScreen(
                            viewModel = aquariumViewModel,
                            aquarium = aquarium,
                            onEdit = {
                                isEditingAquarium = true
                                currentRoute = "edit_aquarium"
                            },
                            onBack = {
                                currentRoute = "my_aquariums"
                                selectedAquarium = null
                            }
                        )
                    }
                    "add_aquarium" -> AddEditAquariumScreen(
                        viewModel = aquariumViewModel,
                        onBack = {
                            currentRoute = "my_aquariums"
                            isAddingAquarium = false
                        }
                    )
                    "edit_aquarium" -> selectedAquarium?.let { aquarium ->
                        AddEditAquariumScreen(
                            viewModel = aquariumViewModel,
                            aquarium = aquarium,
                            onBack = {
                                currentRoute = "aquarium_detail"
                                isEditingAquarium = false
                            }
                        )
                    }
                    "fish_detail" -> selectedFish?.let { fish ->
                        FishDetailScreen(
                            fish = fish,
                            onBackClick = {
                                currentRoute = "fish"
                                selectedFish = null
                            },
                            onDeleteClick = { fishId ->
                                fishViewModel.deleteFish(fishId)
                                currentRoute = "fish"
                                selectedFish = null
                            }
                        )
                    }
                    "my_account" -> ProfileScreen(authViewModel = authViewModel)
                }
            }
        }
        
        // Auth Dialog
        if (showAuthDialog) {
            AuthDialog(
                authViewModel = authViewModel,
                onDismiss = { showAuthDialog = false },
                onAuthSuccess = { showAuthDialog = false }
            )
        }
    }
}