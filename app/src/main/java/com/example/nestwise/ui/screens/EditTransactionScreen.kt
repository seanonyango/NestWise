package com.example.nestwise.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
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
import com.example.nestwise.viewmodel.TransactionViewModel
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTransactionScreen(
    navController: NavController,
    transactionId: String,
    viewModel: TransactionViewModel

) {
    // Collect StateFlow from ViewModel
    val transactions by viewModel.transactions.collectAsState()

    // Find the correct transaction to edit
    val transaction = transactions.find { it.id == transactionId }

    // Handle case: transaction not found (e.g., wrong ID)
    if (transaction == null) {
        Text("Transaction not found.")
        return
    }

    // Editable state fields
    var title by remember { mutableStateOf(transaction.title) }
    var amount by remember { mutableStateOf(transaction.amount.toString()) }
    var category by remember { mutableStateOf(transaction.category) }
    var type by remember { mutableStateOf(transaction.type) }
    var date by remember { mutableStateOf(transaction.date) }

    val isValid = title.isNotBlank() && amount.toDoubleOrNull() != null && category.isNotBlank()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Edit Transaction") })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Title
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") }
            )

            // Amount
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Amount") }
            )

            // Category
            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = { Text("Category") }
            )

            // Type (Income / Expense)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                RadioButton(
                    selected = type == TransactionType.INCOME,
                    onClick = { type = TransactionType.INCOME }
                )
                Text("Income")

                RadioButton(
                    selected = type == TransactionType.EXPENSE,
                    onClick = { type = TransactionType.EXPENSE }
                )
                Text("Expense")
            }

            // Save button
            Button(
                onClick = {
                    val updated = transaction.copy(
                        title = title,
                        amount = amount.toDouble(),
                        category = category,
                        date = date,
                        type = type
                    )
                    viewModel.updateTransaction(updated)
                    navController.popBackStack()
                },
                enabled = isValid
            ) {
                Text("Save Changes")
            }
        }
    }
}

