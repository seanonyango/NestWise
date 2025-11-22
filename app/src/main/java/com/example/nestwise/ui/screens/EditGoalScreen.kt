package com.example.nestwise.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.nestwise.viewmodel.GoalViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditGoalScreen(
    navController: NavController,
    goalId: String,
    viewModel: GoalViewModel
) {
    val goals by viewModel.goals.collectAsState()
    val goal = goals.find { it.id == goalId }

    var title by remember { mutableStateOf(goal?.title ?: "") }
    var target by remember { mutableStateOf(goal?.targetAmount?.toString() ?: "") }
    var current by remember { mutableStateOf(goal?.currentAmount?.toString() ?: "") }
    var deadline by remember { mutableStateOf(goal?.deadline ?: "") }

    val isValid = goal != null &&
            title.isNotBlank() &&
            target.toDoubleOrNull() != null &&
            current.toDoubleOrNull() != null

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Edit Goal") }
            )
        }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Goal Title") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = target,
                onValueChange = { target = it },
                label = { Text("Target Amount") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = current,
                onValueChange = { current = it },
                label = { Text("Amount Saved") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = deadline,
                onValueChange = { deadline = it },
                label = { Text("Deadline (optional)") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    val updated = goal!!.copy(
                        title = title,
                        targetAmount = target.toDouble(),
                        currentAmount = current.toDouble(),
                        deadline = deadline.ifBlank { null }
                    )
                    viewModel.updateGoal(updated)
                    navController.popBackStack()
                },
                enabled = isValid,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Changes")
            }

            OutlinedButton(
                onClick = {
                    viewModel.deleteGoal(goal!!)
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Delete Goal")
            }
        }
    }
}
