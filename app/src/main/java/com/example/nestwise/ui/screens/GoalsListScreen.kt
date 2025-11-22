package com.example.nestwise.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.nestwise.viewmodel.GoalViewModel
import com.example.nestwise.data.entities.GoalEntity
import com.example.nestwise.ui.components.BottomNavBar

@Composable
fun GoalsListScreen(
    navController: NavController,
    viewModel: GoalViewModel
) {
    val goals by viewModel.goals.collectAsState()

    Scaffold(
        bottomBar = { BottomNavBar(navController) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("add_goal") }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Goal")
            }
        }
    ) { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {

            Text(
                "Savings Goals",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(Modifier.height(16.dp))

            if (goals.isEmpty()) {
                Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No savings goals yet. Tap + to add one.")
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(goals.size) { index ->
                        val goal = goals[index]

                        GoalCard(
                            goal = goal,
                            onClick = {
                                navController.navigate("edit_goal/${goal.id}")
                            }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalCard(
    goal: GoalEntity,
    onClick: () -> Unit
) {
    val progress = (goal.currentAmount / goal.targetAmount)
        .coerceIn(0.0, 1.0)

    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                goal.title,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(4.dp))

            Text("Saved: \$${ "%.2f".format(goal.currentAmount)} / \$${goal.targetAmount}")

            Spacer(Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = progress.toFloat(),
                modifier = Modifier.fillMaxWidth()
            )

            if (!goal.deadline.isNullOrEmpty()) {
                Spacer(Modifier.height(6.dp))
                Text(
                    "Deadline: ${goal.deadline}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
