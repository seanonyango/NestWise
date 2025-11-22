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

    if (budget == null) {
        Text("Budget not found")
        return
    }

    // Existing fields
    var category by remember { mutableStateOf(budget.category) }
    var limit by remember { mutableStateOf(budget.limitAmount.toString()) }
    var notes by remember { mutableStateOf(budget.notes ?: "") }

    // NEW Story 3 fields
    val currentSpent = budget.spentAmount

    var overrideSpent by remember { mutableStateOf("") }
    var addSpent by remember { mutableStateOf("") }

    val isValid = category.isNotBlank() && limit.toDoubleOrNull() != null

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

            // ------------------------------
            // EXISTING FIELDS
            // ------------------------------
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

            // ------------------------------
            // NEW SECTION: SPENT AMOUNT EDITING
            // ------------------------------
            Text(
                text = "Current Spent: $${"%.2f".format(currentSpent)}",
                style = MaterialTheme.typography.titleMedium
            )

            OutlinedTextField(
                value = overrideSpent,
                onValueChange = { overrideSpent = it },
                label = { Text("Override spent amount") },
                placeholder = { Text("Leave blank to skip") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = addSpent,
                onValueChange = { addSpent = it },
                label = { Text("Add to spent amount") },
                placeholder = { Text("Leave blank to skip") },
                modifier = Modifier.fillMaxWidth()
            )

            // ------------------------------
            // SAVE BUTTON
            // ------------------------------
            Button(
                onClick = {
                    val updatedSpent = when {
                        overrideSpent.isNotBlank() && overrideSpent.toDoubleOrNull() != null ->
                            overrideSpent.toDouble()

                        addSpent.isNotBlank() && addSpent.toDoubleOrNull() != null ->
                            currentSpent + addSpent.toDouble()

                        else -> currentSpent
                    }

                    val updated = budget.copy(
                        category = category,
                        limitAmount = limit.toDouble(),
                        notes = notes.ifBlank { null },
                        spentAmount = updatedSpent
                    )

                    viewModel.updateBudget(updated)
                    navController.popBackStack()
                },
                enabled = isValid,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Changes")
            }

            // ------------------------------
            // DELETE BUTTON
            // ------------------------------
            OutlinedButton(
                onClick = {
                    viewModel.deleteBudget(budget)
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Delete Budget")
            }
        }
    }
}
