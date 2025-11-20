package com.example.nestwise.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.nestwise.data.entities.BudgetEntity
import com.example.nestwise.viewmodel.BudgetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBudgetScreen(
    navController: NavController,
    budgetId: String,
    viewModel: BudgetViewModel
) {
    val budgets by viewModel.budgets.collectAsState()
    val budget = budgets.find { it.id == budgetId }

    var category by remember { mutableStateOf(budget?.category ?: "") }
    var limit by remember { mutableStateOf(budget?.limitAmount?.toString() ?: "") }
    var notes by remember { mutableStateOf(budget?.notes ?: "") }

    val isValid = budget != null && category.isNotBlank() && limit.toDoubleOrNull() != null

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Edit Budget") })
        }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = { Text("Category") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = limit,
                onValueChange = { limit = it },
                label = { Text("Limit Amount") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Notes") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    val updated = budget!!.copy(
                        category = category,
                        limitAmount = limit.toDouble(),
                        notes = notes.ifBlank { null }
                    )
                    viewModel.updateBudget(updated)
                    navController.popBackStack()
                },
                enabled = isValid,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Changes")
            }

            OutlinedButton(
                onClick = {
                    viewModel.deleteBudget(budget!!)
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Delete Budget")
            }
        }
    }
}
