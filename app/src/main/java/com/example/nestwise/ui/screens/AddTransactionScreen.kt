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
import com.example.nestwise.viewmodel.TransactionViewModel
import com.example.nestwise.data.*
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddTransactionScreen(
    onSaveSuccess: () -> Unit,
    viewModel: TransactionViewModel = viewModel()
) {
    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var type by remember { mutableStateOf(TransactionType.EXPENSE) }
    var date by remember { mutableStateOf(LocalDate.now()) }

    val isValid = title.isNotBlank() && amount.toDoubleOrNull() != null && category.isNotBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Add Transaction", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Title") })
        OutlinedTextField(value = amount, onValueChange = { amount = it }, label = { Text("Amount") })
        OutlinedTextField(value = category, onValueChange = { category = it }, label = { Text("Category") })

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            RadioButton(selected = type == TransactionType.INCOME, onClick = { type = TransactionType.INCOME })
            Text("Income")
            RadioButton(selected = type == TransactionType.EXPENSE, onClick = { type = TransactionType.EXPENSE })
            Text("Expense")
        }

        // date is mandatory for now, prefilled as today

        Button(
            onClick = {
                val newTx = Transaction(
                    title = title,
                    amount = amount.toDouble(),
                    category = category,
                    date = date,
                    type = type
                )
                viewModel.addTransaction(newTx)
                onSaveSuccess()
            },
            enabled = isValid
        ) {
            Text("Save")
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AddTransactionScreenPreview() {
    // Create a mock TransactionViewModel for preview
    val fakeViewModel = TransactionViewModel()

    // Call the AddTransactionScreen with dummy onSaveSuccess
    AddTransactionScreen(
        onSaveSuccess = {},
        viewModel = fakeViewModel
    )
}

