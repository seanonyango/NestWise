package com.example.nestwise.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.nestwise.viewmodel.BudgetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBudgetScreen(
    navController: NavController,
    viewModel: BudgetViewModel
) {
    var category by remember { mutableStateOf("") }
    var limit by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    val isValid = category.isNotBlank() && limit.toDoubleOrNull() != null

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Add Budget") })
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
                label = { Text("Notes (optional)") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    viewModel.addBudget(
                        category = category,
                        limit = limit.toDouble(),
                        notes = notes.ifBlank { null }
                    )
                    navController.popBackStack()
                },
                enabled = isValid,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Budget")
            }
        }
    }
}
