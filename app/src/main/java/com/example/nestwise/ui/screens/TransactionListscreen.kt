package com.example.nestwise.ui.screens

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.nestwise.viewmodel.TransactionViewModel
import com.example.nestwise.data.Transaction
import com.example.nestwise.data.TransactionType
import com.example.nestwise.ui.components.BottomNavBar
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionListScreen(
    navController: NavController,
    viewModel: TransactionViewModel = viewModel()
) {
    val transactions = viewModel.transactions
    val dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")

    Scaffold(
        bottomBar = { BottomNavBar(navController) },  // ✅ Added bottom navigation
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("add_transaction") }) {
                Icon(Icons.Default.Add, contentDescription = "Add Transaction")
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text("Transactions", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(12.dp))

            if (transactions.isEmpty()) {
                Text("No transactions yet. Tap + to add one.")
            } else {
                LazyColumn {
                    items(transactions) { t ->
                        TransactionCard(
                            transaction = t,
                            onEditClick = { /* TODO: Implement Edit */ },
                            onDeleteClick = { viewModel.deleteTransaction(t.id) },
                            dateFormatter = dateFormatter
                        )
                        Spacer(Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TransactionCard(
    transaction: Transaction,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    dateFormatter: DateTimeFormatter
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(transaction.title, style = MaterialTheme.typography.titleMedium)
                Text(transaction.category, style = MaterialTheme.typography.bodySmall)
                Text(transaction.date.format(dateFormatter), style = MaterialTheme.typography.bodySmall)
            }
            Column(horizontalAlignment = androidx.compose.ui.Alignment.End) {
                Text(
                    text = if (transaction.type == TransactionType.INCOME)
                        "+${transaction.amount}" else "-${transaction.amount}",
                    color = if (transaction.type == TransactionType.INCOME)
                        MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                )
                Row {
                    TextButton(onClick = onEditClick) { Text("Edit") }
                    TextButton(onClick = onDeleteClick) { Text("Del") }
                }
            }
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TransactionListScreenPreview() {
    // Create a fake ViewModel with sample data
    val fakeViewModel = TransactionViewModel()

    // Add some mock transactions for the preview
    fakeViewModel.addTransaction(
        Transaction(
            title = "Groceries",
            amount = 45.20,
            category = "Food & Dining",
            date = java.time.LocalDate.now(),
            type = TransactionType.EXPENSE
        )
    )
    fakeViewModel.addTransaction(
        Transaction(
            title = "Paycheck",
            amount = 2500.00,
            category = "Income",
            date = java.time.LocalDate.now(),
            type = TransactionType.INCOME
        )
    )

    // Use a mock NavController for preview (no navigation actions)
    val fakeNavController = androidx.navigation.compose.rememberNavController()

    // Render the screen
    TransactionListScreen(
        navController = fakeNavController,
        viewModel = fakeViewModel
    )
}






