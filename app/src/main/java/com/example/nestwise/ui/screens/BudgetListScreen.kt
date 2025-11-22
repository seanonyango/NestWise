package com.example.nestwise.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.nestwise.data.entities.BudgetEntity
import com.example.nestwise.ui.components.BottomNavBar
import com.example.nestwise.ui.components.BudgetCard
import com.example.nestwise.viewmodel.BudgetViewModel

@Composable
fun BudgetListScreen(
    navController: NavController,
    viewModel: BudgetViewModel
) {
    val budgets by viewModel.budgets.collectAsState()

    Scaffold(
        bottomBar = { BottomNavBar(navController) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("add_budget") }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Budget")
            }
        }
    ) { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Text(
                "Budgets",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.headlineSmall
            )

            if (budgets.isEmpty()) {
                Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    Text("No budgets yet. Tap + to add one.")
                }
            } else {
                LazyColumn {
                    items(budgets.size) { index ->
                        val budget: BudgetEntity = budgets[index]

                        // TEMP: spent = 0 until later integration with actual transactions
                        BudgetCard(
                            budget = budget,
                            onClick = {
                                navController.navigate("edit_budget/${budget.id}")
                            }
                        )
                    }
                }
            }
        }
    }
}
