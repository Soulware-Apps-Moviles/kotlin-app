package com.soulware.tcompro.features.shop.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.soulware.tcompro.R
import com.soulware.tcompro.core.ui.theme.TcomproTheme

private val successGreen = Color(0xFF4CAF50)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEmployeeByCodeScreen(
    navController: NavHostController,
    viewModel: AddEmployeeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val validationStatus = uiState.status
    val foundEmployee = uiState.foundEmployee

    // Variable para el Email
    var email by remember { mutableStateOf("") }

    val areBottomButtonsEnabled = validationStatus == ValidationStatus.SUCCESS

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Agregar Empleado") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Go back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Black,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.height(48.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                ) {
                    Text("Cancelar")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.height(48.dp),
                    enabled = areBottomButtonsEnabled,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White
                    )
                ) {
                    Text("Confirmar")
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column {
                    // Cabecera de la tarjeta (Foto y Nombre)
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (validationStatus == ValidationStatus.SUCCESS) {
                            Image(
                                painter = painterResource(id = R.drawable.app_logo),
                                contentDescription = "Employee Photo",
                                modifier = Modifier.size(56.dp).clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "New Employee",
                                modifier = Modifier.size(56.dp),
                                tint = Color.Black
                            )
                        }
                        Spacer(modifier = Modifier.padding(8.dp))
                        Column {
                            Text(
                                text = if (validationStatus == ValidationStatus.SUCCESS) foundEmployee?.name ?: "Encontrado" else "Nuevo Empleado",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = if (validationStatus == ValidationStatus.SUCCESS) foundEmployee?.role ?: "Shopkeeper" else "Nombre del empleado",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }
                    }

                    // Formulario
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Ingresa el correo electrónico del empleado registrado para contratarlo.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            OutlinedTextField(
                                value = email,
                                onValueChange = {
                                    email = it
                                    if (validationStatus != ValidationStatus.IDLE) {
                                        viewModel.resetState()
                                    }
                                },
                                label = { Text("Correo electrónico") },
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                                    unfocusedLabelColor = Color.Gray
                                )
                            )
                            Spacer(modifier = Modifier.padding(4.dp))

                            Button(
                                onClick = {
                                    viewModel.hireEmployeeByEmail(email)
                                },
                                enabled = email.isNotBlank() && validationStatus != ValidationStatus.LOADING,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = Color.White
                                )
                            ) {
                                if (validationStatus == ValidationStatus.LOADING) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(24.dp),
                                        color = Color.White,
                                        strokeWidth = 2.dp
                                    )
                                } else {
                                    Text("Validar")
                                }
                            }
                        }

                        // Mensajes de estado
                        when (validationStatus) {
                            ValidationStatus.SUCCESS -> {
                                Text(
                                    "¡Empleado contratado exitosamente!",
                                    color = successGreen,
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            }
                            ValidationStatus.ERROR -> {
                                Text(
                                    uiState.errorMessage,
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            }
                            else -> {}
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddEmployeeByCodeScreenPreview() {
    TcomproTheme {
        AddEmployeeByCodeScreen(rememberNavController())
    }
}