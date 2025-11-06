package com.example.nestwise.ui.screens

import android.annotation.SuppressLint
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
    viewModel: TransactionViewModel = viewModel()
) {
    // Find the transaction by ID
    val transaction = viewModel.transactions.find { it.id == transactionId }

    // Local state copies for editable fields
    var title by remember { mutableStateOf(transaction?.title ?: "") }
    var amount by remember { mutableStateOf(transaction?.amount?.toString() ?: "") }
    var category by remember { mutableStateOf(transaction?.category ?: "") }
    var type by remember { mutableStateOf(transaction?.type ?: TransactionType.EXPENSE) }
    var date by remember { mutableStateOf(transaction?.date ?: LocalDate.now()) }

    val isValid = title.isNotBlank() && amount.toDoubleOrNull() != null && category.isNotBlank()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Edit Transaction") }
            )
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
            OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Title") })
            OutlinedTextField(value = amount, onValueChange = { amount = it }, label = { Text("Amount") })
            OutlinedTextField(value = category, onValueChange = { category = it }, label = { Text("Category") })

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                RadioButton(selected = type == TransactionType.INCOME, onClick = { type = TransactionType.INCOME })
                Text("Income")
                RadioButton(selected = type == TransactionType.EXPENSE, onClick = { type = TransactionType.EXPENSE })
                Text("Expense")
            }

            Button(
                onClick = {
                    val updated = transaction?.copy(
                        title = title,
                        amount = amount.toDouble(),
                        category = category,
                        date = date,
                        type = type
                    )
                    if (updated != null) {
                        viewModel.updateTransaction(updated)
                    }
                    navController.popBackStack() // Return to list
                },
                enabled = isValid
            ) {
                Text("Save Changes")
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EditTransactionScreenPreview() {
    val fakeViewModel = TransactionViewModel()

    // Create a sample transaction
    val sample = Transaction(
        title = "Groceries",
        amount = 55.0,
        category = "Food & Dining",
        date = LocalDate.now(),
        type = TransactionType.EXPENSE
    )
    fakeViewModel.addTransaction(sample)

    val fakeNavController = androidx.navigation.compose.rememberNavController()

    EditTransactionScreen(
        navController = fakeNavController,
        transactionId = sample.id,
        viewModel = fakeViewModel
    )
}

