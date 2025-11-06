package com.example.nestwise.ui.screens

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import kotlinx.coroutines.launch
import androidx.compose.foundation.clickable



@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionListScreen(
    navController: NavController,
    viewModel: TransactionViewModel = viewModel()
) {
    val transactions = viewModel.transactions
    val dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // 👇 NEW: track selected IDs and whether selection mode is active
    val selectedTransactions = remember { mutableStateListOf<String>() }
    val isSelecting = selectedTransactions.isNotEmpty()

    Scaffold(
        bottomBar = { BottomNavBar(navController) },
        floatingActionButton = {
            if (isSelecting) {
                ExtendedFloatingActionButton(
                    text = { Text("Delete Selected") },
                    icon = { Icon(Icons.Default.Delete, contentDescription = "Delete") },
                    onClick = {
                        viewModel.deleteMultiple(selectedTransactions.toList())
                        selectedTransactions.clear()
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Selected transactions deleted")
                        }
                    }
                )
            } else {
                FloatingActionButton(onClick = { navController.navigate("add_transaction") }) {
                    Icon(Icons.Default.Add, contentDescription = "Add Transaction")
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
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
                        val isSelected = t.id in selectedTransactions

                        TransactionSelectableCard(
                            transaction = t,
                            isSelected = isSelected,
                            onSelect = {
                                if (isSelected) selectedTransactions.remove(t.id)
                                else selectedTransactions.add(t.id)
                            },
                            onEditClick = { navController.navigate("edit_transaction/${t.id}") },
                            onDeleteClick = {
                                viewModel.deleteTransaction(t.id)
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Transaction deleted")
                                }
                            },
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
            modifier = Modifier
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
                    IconButton(onClick = onEditClick) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                    IconButton(onClick = onDeleteClick) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }
                }
            }
        }
    }
}


//@SuppressLint("ViewModelConstructorInComposable")
//@RequiresApi(Build.VERSION_CODES.O)
//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun TransactionListScreenPreview() {
//    // Create a fake ViewModel with sample data
//    val fakeViewModel = TransactionViewModel()
//
//    // Add some mock transactions for the preview
//    fakeViewModel.addTransaction(
//        Transaction(
//            title = "Groceries",
//            amount = 45.20,
//            category = "Food & Dining",
//            date = java.time.LocalDate.now(),
//            type = TransactionType.EXPENSE
//        )
//    )
//    fakeViewModel.addTransaction(
//        Transaction(
//            title = "Paycheck",
//            amount = 2500.00,
//            category = "Income",
//            date = java.time.LocalDate.now(),
//            type = TransactionType.INCOME
//        )
//    )
//
//    // Use a mock NavController for preview (no navigation actions)
//    val fakeNavController = androidx.navigation.compose.rememberNavController()
//
//    // Render the screen
//    TransactionListScreen(
//        navController = fakeNavController,
//        viewModel = fakeViewModel
//    )
//}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TransactionSelectableCard(
    transaction: Transaction,
    isSelected: Boolean,
    onSelect: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    dateFormatter: DateTimeFormatter
) {
    val bgColor = if (isSelected)
        MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
    else
        MaterialTheme.colorScheme.surfaceVariant

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                enabled = true,
                role = null,
                onClick = { onSelect() }
            ), // 👈 toggle selection
        colors = CardDefaults.cardColors(containerColor = bgColor)
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
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = if (transaction.type == TransactionType.INCOME)
                        "+${transaction.amount}" else "-${transaction.amount}",
                    color = if (transaction.type == TransactionType.INCOME)
                        MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                )
                Row {
                    IconButton(onClick = onEditClick) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                    IconButton(onClick = onDeleteClick) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TransactionListMultiSelectPreview() {
    val fakeViewModel = TransactionViewModel()
    fakeViewModel.addTransaction(
        Transaction("", "Groceries", 45.20, "Food & Dining", java.time.LocalDate.now(), TransactionType.EXPENSE)
    )
    fakeViewModel.addTransaction(
        Transaction("","Paycheck", 2500.00, "Income", java.time.LocalDate.now(), TransactionType.INCOME)
    )
    val fakeNavController = androidx.navigation.compose.rememberNavController()

    TransactionListScreen(navController = fakeNavController, viewModel = fakeViewModel)
}








