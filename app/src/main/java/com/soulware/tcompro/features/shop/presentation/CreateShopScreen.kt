package com.soulware.tcompro.features.shop.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.soulware.tcompro.core.MainTabRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateShopScreen(
    navController: NavHostController,
    userEmail: String,
    viewModel: CreateShopViewModel = hiltViewModel()
) {
    var name by remember { mutableStateOf("") }

    val lima = LatLng(-12.046374, -77.042793)
    var selectedLocation by remember { mutableStateOf(lima) }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(lima, 15f)
    }

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.status) {
        if (uiState.status == CreateShopStatus.SUCCESS) {
            navController.navigate("main_container") {
                popUpTo("login") { inclusive = true }
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Ubicación de tu Tienda", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Black,
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("1. Ingresa el nombre de tu negocio")
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre de la Tienda") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }

            Text(
                "2. Selecciona la ubicación exacta en el mapa",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    onMapClick = { latLng ->
                        selectedLocation = latLng
                    }
                ) {
                    Marker(
                        state = MarkerState(position = selectedLocation),
                        title = "Mi Tienda",
                        snippet = "Ubicación seleccionada"
                    )
                }

                Text(
                    text = "Lat: ${selectedLocation.latitude}, Lng: ${selectedLocation.longitude}",
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .background(Color.White.copy(alpha = 0.7f))
                        .padding(4.dp),
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Column(modifier = Modifier.padding(16.dp)) {
                if (uiState.status == CreateShopStatus.ERROR) {
                    Text(uiState.errorMessage, color = MaterialTheme.colorScheme.error)
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Button(
                    onClick = {
                        viewModel.createShop(
                            name,
                            selectedLocation.latitude,
                            selectedLocation.longitude,
                            userEmail
                        )
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    enabled = name.isNotBlank() && uiState.status != CreateShopStatus.LOADING,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                ) {
                    if (uiState.status == CreateShopStatus.LOADING) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Text("Confirmar y Crear")
                    }
                }
            }
        }
    }
}