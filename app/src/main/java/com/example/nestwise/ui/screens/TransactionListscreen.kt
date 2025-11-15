package com.example.nestwise.ui.screens

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
import com.example.nestwise.data.Transaction
import com.example.nestwise.data.TransactionType
import com.example.nestwise.ui.components.BottomNavBar
import com.example.nestwise.viewmodel.TransactionViewModel
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionListScreen(
    navController: NavController,
    viewModel: TransactionViewModel
){
    // FIX 1: Collect StateFlow from ViewModel
    val transactions by viewModel.transactions.collectAsState()

    val dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Multi-select tracking
    val selected = remember { mutableStateListOf<String>() }
    val isSelecting = selected.isNotEmpty()

    Scaffold(
        bottomBar = { BottomNavBar(navController) },
        floatingActionButton = {
            if (isSelecting) {
                ExtendedFloatingActionButton(
                    text = { Text("Delete Selected") },
                    icon = { Icon(Icons.Default.Delete, contentDescription = null) },
                    onClick = {
                        viewModel.deleteMultiple(selected)
                        selected.clear()
                        scope.launch {
                            snackbarHostState.showSnackbar("Selected transactions deleted")
                        }
                    }
                )
            } else {
                FloatingActionButton(
                    onClick = { navController.navigate("add_transaction") }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Transaction")
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("Transactions", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(12.dp))

            if (transactions.isEmpty()) {
                Text("No transactions yet. Tap + to add one.")
            } else {
                LazyColumn {
                    items(transactions) { t ->
                        val isSelected = t.id in selected

                        TransactionSelectableCard(
                            transaction = t,
                            isSelected = isSelected,
                            onSelect = {
                                if (isSelected) selected.remove(t.id)
                                else selected.add(t.id)
                            },
                            onEditClick = {
                                navController.navigate("edit_transaction/${t.id}")
                            },
                            onDeleteClick = {
                                viewModel.deleteTransaction(t.id)
                                scope.launch {
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
            ),
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
                        "+${transaction.amount}"
                    else "-${transaction.amount}",
                    color = if (transaction.type == TransactionType.INCOME)
                        MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.error
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
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TransactionListScreenPreview() {
    val fakeViewModel = TransactionViewModel.preview()
    val fakeNavController = androidx.navigation.compose.rememberNavController()

    TransactionListScreen(
        navController = fakeNavController,
        viewModel = fakeViewModel
    )
}

