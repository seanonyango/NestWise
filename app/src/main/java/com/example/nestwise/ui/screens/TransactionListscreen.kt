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
import androidx.compose.material.icons.filled.MoreVert
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
import com.example.nestwise.data.mappers.toEntity
import com.example.nestwise.ui.components.BottomNavBar
import com.example.nestwise.viewmodel.BudgetViewModel
import com.example.nestwise.viewmodel.TransactionViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionListScreen(
    navController: NavController,
    transactionViewModel: TransactionViewModel,
    budgetViewModel: BudgetViewModel
) {
    val transactions by transactionViewModel.transactions.collectAsState()
    val budgets by budgetViewModel.budgets.collectAsState()

    val dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Dialog state
    var showBudgetDialog by remember { mutableStateOf(false) }
    var selectedTransactionForBudget by remember { mutableStateOf<Transaction?>(null) }

    Scaffold(
        bottomBar = { BottomNavBar(navController) },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("add_transaction") }) {
                Icon(Icons.Default.Add, contentDescription = "Add Transaction")
            }
        }
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
                        TransactionSelectableCard(
                            transaction = t,
                            dateFormatter = dateFormatter,
                            onEditClick = {
                                navController.navigate("edit_transaction/${t.id}")
                            },
                            onDeleteClick = {
                                transactionViewModel.deleteTransaction(t.id)
                                scope.launch {
                                    snackbarHostState.showSnackbar("Transaction deleted")
                                }
                            },
                            onAddToBudgetClick = {
                                selectedTransactionForBudget = t
                                showBudgetDialog = true
                            },
                            onRemoveFromBudgetClick = { budgetId ->
                                budgetViewModel.unlinkTransactionFromBudget(
                                    budgetId = budgetId,
                                    transaction = t.toEntity()
                                )
                                scope.launch { snackbarHostState.showSnackbar("Removed from budget") }
                            },
                            isLinkedToBudget = runBlocking {
                                budgetViewModel.getLinkedBudget(t.id) != null
                            }
                        )
                        Spacer(Modifier.height(8.dp))
                    }
                }
            }
        }
    }

    // -------------------------------
    // Budget Selection Dialog
    // -------------------------------
    if (showBudgetDialog && selectedTransactionForBudget != null) {
        val matchingBudgets = budgets.filter {
            it.category.equals(selectedTransactionForBudget!!.category, ignoreCase = true)
        }

        AlertDialog(
            onDismissRequest = { showBudgetDialog = false },
            title = { Text("Assign to Budget") },
            text = {
                if (matchingBudgets.isEmpty()) {
                    Text("No budgets exist for this category.")
                } else {
                    Column {
                        matchingBudgets.forEach { budget ->
                            TextButton(onClick = {
                                budgetViewModel.linkTransactionToBudget(
                                    budgetId = budget.id,
                                    transaction = selectedTransactionForBudget!!.toEntity()
                                )
                                showBudgetDialog = false
                            }) {
                                Text(budget.category)
                            }
                        }
                    }
                }
            },
            confirmButton = {}
        )
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TransactionSelectableCard(
    transaction: Transaction,
    dateFormatter: DateTimeFormatter,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onAddToBudgetClick: () -> Unit,
    onRemoveFromBudgetClick: (String) -> Unit,
    isLinkedToBudget: Boolean
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Left side
            Column {
                Text(transaction.title, style = MaterialTheme.typography.titleMedium)
                Text(transaction.category, style = MaterialTheme.typography.bodySmall)
                Text(transaction.date.format(dateFormatter), style = MaterialTheme.typography.bodySmall)
            }

            // Right side
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    if (transaction.type == TransactionType.INCOME) "+${transaction.amount}"
                    else "-${transaction.amount}",
                    color = if (transaction.type == TransactionType.INCOME)
                        MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.error
                )

                // Icons row
                Row {
                    IconButton(onClick = onEditClick) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                    IconButton(onClick = onDeleteClick) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }

                    // ⋮ menu
                    IconButton(onClick = { menuExpanded = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "More Options")
                    }

                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false }
                    ) {
                        if (!isLinkedToBudget) {
                            DropdownMenuItem(
                                text = { Text("Add to Budget") },
                                onClick = {
                                    menuExpanded = false
                                    onAddToBudgetClick()
                                }
                            )
                        } else {
                            DropdownMenuItem(
                                text = { Text("Remove from Budget") },
                                onClick = {
                                    menuExpanded = false
                                    // The ViewModel will know which budget to unlink
                                    onRemoveFromBudgetClick(
                                        transaction.id // we fetch actual budget in VM
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}


//@RequiresApi(Build.VERSION_CODES.O)
//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun TransactionListScreenPreview() {
//    val fakeViewModel = TransactionViewModel.preview()
//    val fakeNavController = androidx.navigation.compose.rememberNavController()
//
//    TransactionListScreen(
//        navController = fakeNavController,
//        viewModel = fakeViewModel
//    )
//}

