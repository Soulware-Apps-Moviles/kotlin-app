package com.soulware.tcompro.features.orders.presentation.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.soulware.tcompro.features.orders.domain.models.Order
import com.soulware.tcompro.features.orders.domain.models.OrderLine

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailScreen(
    orderId: Int,
    onBackClick: () -> Unit = {}
) {
    val viewModel: OrderDetailViewModel = hiltViewModel()
    val order by viewModel.order.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    LaunchedEffect(orderId) {
        viewModel.loadOrder(orderId)
    }

    when {
        isLoading -> Box(Modifier.fillMaxSize(), Alignment.Center) {
            CircularProgressIndicator()
        }

        errorMessage != null -> Box(Modifier.fillMaxSize(), Alignment.Center) {
            Text(errorMessage ?: "Unexpected error", color = MaterialTheme.colorScheme.error)
        }

        order != null -> OrderDetailContent(
            order = order!!,
            onBackClick = onBackClick,
            onCancelOrder = { viewModel.cancelOrder(orderId) },
            onReadyForPickup = { viewModel.advanceOrder(orderId) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailContent(
    order: Order,
    onBackClick: () -> Unit,
    onCancelOrder: (Int) -> Unit,
    onReadyForPickup: (Int) -> Unit
) {
    var showCancelDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Order #${order.id}",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    Text(
                        text = "Accepted",
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(horizontal = 10.dp, vertical = 4.dp),
                        fontSize = 12.sp
                    )
                }
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { showCancelDialog = true },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Cancel order")
                }

                Button(
                    onClick = { onReadyForPickup(order.id) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text("Ready for pick up")
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Customer photo",
                        modifier = Modifier
                            .size(48.dp)
                            .clip(MaterialTheme.shapes.small)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("New customer!", color = MaterialTheme.colorScheme.primary, fontSize = 12.sp)
                        Text("John Doe", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Order items",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Store, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(order.pickupMethod)
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Money, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(order.paymentMethod)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                HeaderRow()
                Spacer(modifier = Modifier.height(4.dp))
            }
            items(order.orderLines) { line ->
                OrderItemRow(line)
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                )
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.primary
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                        ) {
                            Text("Total", color = Color.White, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                "S/${"%.2f".format(order.orderLines.sumOf { it.price * it.quantity })}",
                                color = Color.White,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }

    if (showCancelDialog) {
        CancelOrderDialog(
            onDismiss = { showCancelDialog = false },
            onConfirm = {
                showCancelDialog = false
                onCancelOrder(order.id)
            }
        )
    }
}

@Composable
private fun HeaderRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("Product", fontWeight = FontWeight.SemiBold)
        Text("Units", fontWeight = FontWeight.SemiBold)
        Text("Unit Price", fontWeight = FontWeight.SemiBold)
        Text("Subtotal", fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun OrderItemRow(line: OrderLine) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AsyncImage(
            model = line.imageUrl,
            contentDescription = line.name,
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(4.dp))
        )
        Text(
            text = line.name,
            modifier = Modifier.weight(1.3f),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "${line.quantity}",
            modifier = Modifier.weight(0.4f),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "S/${"%.2f".format(line.price)}",
            modifier = Modifier.weight(0.7f),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = "S/${"%.2f".format(line.price * line.quantity)}",
            modifier = Modifier.weight(0.8f),
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun CancelOrderDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            tonalElevation = 6.dp,
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Confirm cancellation", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Are you sure you want to proceed?\nCustomer will be notified.",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Go back")
                    }
                    Button(
                        onClick = onConfirm,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Cancel order")
                    }
                }
            }
        }
    }
}

