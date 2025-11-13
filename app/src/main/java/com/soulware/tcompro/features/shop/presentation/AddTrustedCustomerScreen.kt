package com.soulware.tcompro.features.shop.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.soulware.tcompro.core.ui.theme.TcomproTheme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// --- Estados para la UI (reutilizamos los de Staff) ---
enum class CustomerValidationStatus { IDLE, LOADING, SUCCESS, ERROR }
data class AddCustomerState(
    val status: CustomerValidationStatus = CustomerValidationStatus.IDLE,
    val foundCustomerName: String? = null,
    val errorMessage: String = ""
)
private val successGreen = Color(0xFF4CAF50)

// --- ViewModel Placeholder (En "stand by") ---
@HiltViewModel
class AddCustomerViewModel @Inject constructor(
    // TODO: Inyectar el Repository
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(AddCustomerState())
    val uiState: StateFlow<AddCustomerState> = _uiState.asStateFlow()

    var email by mutableStateOf("")
        private set

    init {
        val scannedEmail: String? = savedStateHandle.get("scannedEmail")
        if (scannedEmail != null) {
            email = scannedEmail
            validateCustomer(scannedEmail)
        }
    }

    fun onEmailChange(newEmail: String) {
        email = newEmail
        if (_uiState.value.status != CustomerValidationStatus.IDLE) {
            _uiState.value = AddCustomerState()
        }
    }

    fun validateCustomer(emailToValidate: String) {
        viewModelScope.launch {
            _uiState.value = AddCustomerState(status = CustomerValidationStatus.LOADING)
            // TODO: Aquí va la lógica bloqueada por el backend:
            // 1. val profile = profileRepo.getProfileByEmail(emailToValidate)
            // 2. if (profile == null) { _uiState.value = AddCustomerState(status=ERROR) }
            // 3. val customer = customerRepo.addTrustedCustomer(shopId, profile.authId)
            // 4. _uiState.value = AddCustomerState(status=SUCCESS, foundCustomerName = customer.name)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTrustedCustomerScreen(
    navController: NavHostController,
    viewModel: AddCustomerViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val validationStatus = uiState.status
    val areBottomButtonsEnabled = validationStatus == CustomerValidationStatus.SUCCESS

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Add Trusted Customer",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Go back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "More options")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Black,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
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
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                ) {
                    Text("Cancel")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = {
                        navController.popBackStack()
                    },
                    modifier = Modifier.height(48.dp),
                    enabled = areBottomButtonsEnabled,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White
                    )
                ) {
                    Text("Confirm")
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
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "New Customer",
                            modifier = Modifier.size(56.dp),
                            tint = Color.Black
                        )
                        Spacer(modifier = Modifier.padding(8.dp))
                        Column {
                            Text(
                                text = uiState.foundCustomerName ?: "New Customer",
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (validationStatus == CustomerValidationStatus.SUCCESS) MaterialTheme.colorScheme.primary else Color.Gray
                            )
                            Text(
                                text = "Customer Name",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }
                    }

                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Enter user email to add a trusted customer",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            OutlinedTextField(
                                value = viewModel.email,
                                onValueChange = { viewModel.onEmailChange(it) },
                                label = { Text("Enter user email") },
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                                    unfocusedLabelColor = Color.Gray
                                )
                            )
                            Spacer(modifier = Modifier.padding(4.dp))
                            Button(
                                onClick = {
                                    viewModel.validateCustomer(viewModel.email)
                                },
                                enabled = viewModel.email.isNotBlank() && validationStatus != CustomerValidationStatus.LOADING,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = Color.White
                                )
                            ) {
                                if (validationStatus == CustomerValidationStatus.LOADING) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(24.dp),
                                        color = Color.White,
                                        strokeWidth = 2.dp
                                    )
                                } else {
                                    Text("Validate")
                                }
                            }
                        }
                        when (validationStatus) {
                            CustomerValidationStatus.SUCCESS -> {
                                Text(
                                    "Customer found",
                                    color = successGreen,
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            }
                            CustomerValidationStatus.ERROR -> {
                                Text(
                                    uiState.errorMessage.ifEmpty { "No user found with that email" },
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
fun AddTrustedCustomerScreenPreview() {
    TcomproTheme {
        AddTrustedCustomerScreen(rememberNavController())
    }
}