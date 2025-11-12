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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.soulware.tcompro.features.finances.domain.models.Debt
import com.soulware.tcompro.features.finances.domain.models.Payment

@Composable
fun FinancesScreen(
    viewModel: FinancesViewModel = hiltViewModel()
) {
    val payments by viewModel.payments.collectAsState()
    val debts by viewModel.debts.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadFinances()
    }

    when {
        isLoading -> Box(Modifier.fillMaxSize(), Alignment.Center) {
            CircularProgressIndicator()
        }

        errorMessage != null -> Box(Modifier.fillMaxSize(), Alignment.Center) {
            Text(errorMessage ?: "Unexpected error", color = MaterialTheme.colorScheme.error)
        }

        else -> FinancesContent(
            payments = payments,
            debts = debts,
            onMarkPaid = viewModel::markDebtAsPaid
        )
    }
}

@Composable
private fun FinancesContent(
    payments: List<Payment>,
    debts: List<Debt>,
    onMarkPaid: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Row(
            Modifier.fillMaxWidth(),
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
                contentDescription = "Stats",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(28.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // TOTAL REVENUE
        TotalRevenueCard(payments)

        Spacer(modifier = Modifier.height(30.dp))

        // CREDITORS
        SectionTitle("Creditors")

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 240.dp)
        ) {
            items(debts.filter { it.status == "PENDING" }) { debt ->
                CreditorItem(
                    name = "Customer ${debt.customerId}",
                    amount = "S/${debt.amount}",
                    status = debt.status,
                    onMarkPaid = { onMarkPaid(debt.id) }
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        // TRANSACTION HISTORY
        SectionTitle("Transaction History")

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(payments) { payment ->
                TransactionItem(
                    title = "Payment #${payment.id}",
                    date = "Order: ${payment.orderId}",
                    amount = "+S/${payment.amount}",
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun TotalRevenueCard(payments: List<Payment>) {
    val total = payments.sumOf { it.amount }

    Surface(
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                "Total Revenue",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 16.sp
            )
            Text(
                "S/ ${"%.2f".format(total)}",
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
            color = color
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
