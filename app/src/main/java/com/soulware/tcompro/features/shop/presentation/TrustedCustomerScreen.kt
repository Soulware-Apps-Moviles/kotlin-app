package com.soulware.tcompro.features.shop.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.soulware.tcompro.R
import com.soulware.tcompro.core.ui.theme.TcomproTheme
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

// --- ViewModel Placeholder (En "stand by") ---
@HiltViewModel
class TrustedCustomerViewModel @Inject constructor() : ViewModel() {
    // TODO: Cargar la lista de clientes desde el Repository
    // val customers = ...
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrustedCustomerScreen(
    navController: NavHostController,
    viewModel: TrustedCustomerViewModel = hiltViewModel()
) {
    var isFabMenuExpanded by remember { mutableStateOf(false) }

    // Por ahora, mostramos el estado vacío
    val customers = emptyList<Any>()

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
            ) {
                OutlinedTextField(
                    value = "",
                    onValueChange = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    placeholder = { Text("Search customer") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )

                if (customers.isEmpty()) {
                    EmptyCustomerView()
                } else {
                    // TODO: Usar LazyColumn para mostrar 'CustomerItem'
                }
            }
        }

        if (isFabMenuExpanded) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { isFabMenuExpanded = false }
                    ),
                color = Color.Black.copy(alpha = 0.6f)
            ) {}
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            horizontalAlignment = Alignment.End,
        ) {
            if (isFabMenuExpanded) {
                SpeedDialButton(
                    text = "Scan customer QR",
                    icon = Icons.Default.QrCode,
                    onClick = {
                        navController.navigate("add_customer_qr")
                        isFabMenuExpanded = false
                    }
                )
                Spacer(modifier = Modifier.height(24.dp))

                SpeedDialButton(
                    text = "Enter customer email",
                    icon = Icons.Default.Email,
                    onClick = {
                        navController.navigate("add_customer_email")
                        isFabMenuExpanded = false
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            ExtendedFloatingActionButton(
                text = { Text("New customer", style = MaterialTheme.typography.labelLarge) },
                icon = {
                    val icon = if (isFabMenuExpanded) Icons.Default.Close else Icons.Default.PersonAdd
                    Icon(icon, contentDescription = "New Customer")
                },
                onClick = {
                    isFabMenuExpanded = !isFabMenuExpanded
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
private fun EmptyCustomerView() {
    Box(
        modifier = Modifier.fillMaxSize().padding(top = 32.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.app_logo),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(0.8f),
            contentScale = ContentScale.Fit,
            alpha = 0.1f
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 24.dp)
        ) {
            Text(
                text = "No trusted customers yet",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Add customers by email to get started!",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun SpeedDialButton(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = onClick
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = Color.White,
            modifier = Modifier.padding(end = 12.dp)
        )
        Surface(
            shape = CircleShape,
            color = Color.White,
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = Color.Black,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TrustedCustomerScreenPreview() {
    TcomproTheme {
        TrustedCustomerScreen(rememberNavController())
    }
}