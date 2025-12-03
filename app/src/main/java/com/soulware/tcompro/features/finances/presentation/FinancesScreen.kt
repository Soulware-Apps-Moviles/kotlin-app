package com.soulware.tcompro.features.finances.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.soulware.tcompro.R
import com.soulware.tcompro.core.ITabRoute
import com.soulware.tcompro.core.TwoTabScreen
import com.soulware.tcompro.features.finances.domain.models.Debt
import com.soulware.tcompro.features.finances.domain.models.Payment

// ---------------------------
// Tabs
// ---------------------------
sealed class FinancesTab(
    override val route: String,
    @androidx.annotation.StringRes override val labelResId: Int
) : ITabRoute {

    object Creditors : FinancesTab("creditors_tab", R.string.label_creditors)
    object History : FinancesTab("history_tab", R.string.label_transaction_history)
}

// ---------------------------
// Screen
// ---------------------------
@Composable
fun FinancesScreen(
    viewModel: FinancesViewModel = hiltViewModel()
) {
    val payments by viewModel.payments.collectAsState()
    val debts by viewModel.debts.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val totalRevenue by viewModel.totalRevenue.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadFinances()
    }

    val tabs = listOf(
        FinancesTab.Creditors,
        FinancesTab.History
    )

    when {
        isLoading -> Box(Modifier.fillMaxSize(), Alignment.Center) {
            CircularProgressIndicator()
        }

        errorMessage != null -> Box(Modifier.fillMaxSize(), Alignment.Center) {
            Text(text = errorMessage ?: "Unexpected error", color = MaterialTheme.colorScheme.error)
        }

        else -> Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Finances",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 26.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
                Icon(
                    imageVector = Icons.Outlined.BarChart,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(28.dp)
                )
            }

            TotalRevenueCard(totalRevenue)

            Spacer(Modifier.height(20.dp))

            // ---------------------------
            // Tabs (correct usage)
            // ---------------------------
            TwoTabScreen(
                tabs = tabs, // <-- FIXED: pass ITabRoute list directly
                content1 = {
                    CreditorsTab(
                        debts = debts,
                        onMarkPaid = viewModel::markDebtAsPaid
                    )
                },
                content2 = {
                    HistoryTab(
                        payments = payments
                    )
                }
            )
        }
    }
}

// ---------------------------
// Tabs Content
// ---------------------------
@Composable
private fun CreditorsTab(
    debts: List<Debt>,
    onMarkPaid: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        SectionTitle(text = stringResource(id = R.string.label_creditors))
        Spacer(Modifier.height(12.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(debts.filter { it.status == "PENDING" }) { debt ->
                CreditorItem(
                    name = "Customer ${debt.customerId}",
                    amount = "S/${"%.2f".format(debt.amount)}",
                    status = debt.status,
                    onMarkPaid = { onMarkPaid(debt.id) }
                )
            }
        }
    }
}

@Composable
private fun HistoryTab(
    payments: List<Payment>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        SectionTitle(text = stringResource(id = R.string.label_transaction_history))
        Spacer(Modifier.height(12.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(payments) { payment ->
                TransactionItem(
                    title = "Payment #${payment.id}",
                    date = "Order: ${payment.orderId}",
                    amount = "+S/${"%.2f".format(payment.amount)}",
                    color = Color(0xFF2E7D32)
                )
            }
        }
    }
}

// ---------------------------
// Components
// ---------------------------
@Composable
private fun TotalRevenueCard(totalRevenue: Double) {
    Surface(
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("Total Revenue", color = MaterialTheme.colorScheme.primary, fontSize = 16.sp)
            Text(
                "S/ ${"%.2f".format(totalRevenue)}",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 26.sp
            )
        }
    }
}

@Composable
fun CreditorItem(
    name: String,
    amount: String,
    status: String,
    onMarkPaid: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(name, style = MaterialTheme.typography.bodyLarge)
            Text(
                status,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
            Text(
                amount,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
        }

        Button(
            onClick = onMarkPaid,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text("Mark paid")
        }
    }
}

@Composable
fun TransactionItem(
    title: String,
    date: String,
    amount: String,
    color: Color
) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(title, style = MaterialTheme.typography.bodyLarge)
            Text(
                date,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }

        Text(
            text = amount,
            color = color,
            fontSize = 18.sp
        )
    }
}

@Composable
fun SectionTitle(text: String) {
    Text(
        text = text,
        color = MaterialTheme.colorScheme.primary,
        fontSize = 18.sp
    )
}
