package com.example.nestwise.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.nestwise.data.Transaction
import com.example.nestwise.data.TransactionType
import com.example.nestwise.ui.navigation.NavRoutes
import com.example.nestwise.viewmodel.TransactionViewModel
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddTransactionScreen(
    onSaveSuccess: () -> Unit,
    viewModel: TransactionViewModel,
    navController: NavController

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
        OutlinedButton(
            onClick = { navController.navigate(NavRoutes.ImportCsv.route) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.FileUpload, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Import from CSV")
        }

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

//@RequiresApi(Build.VERSION_CODES.O)
//@SuppressLint("ViewModelConstructorInComposable")
//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun AddTransactionScreenPreview() {
//    // Create a mock TransactionViewModel for preview
//    val fakeViewModel = TransactionViewModel()
//
//    // Call the AddTransactionScreen with dummy onSaveSuccess
//    AddTransactionScreen(
//        onSaveSuccess = {},
//        viewModel = fakeViewModel
//    )
//}

