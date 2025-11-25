package com.example.nestwise.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    val primaryBlue = Color(0xFF1565C0)
//    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        bottomBar = { BottomNavBar(navController) },
//        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("add_budget") }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Budget")
            }
        }
    ) {  padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                "🪺 NestWise",
                color = primaryBlue,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                "Budgets",
                modifier = Modifier.padding(12.dp),
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
