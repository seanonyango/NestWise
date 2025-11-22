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
fun AddGoalScreen(
    navController: NavController,
    viewModel: GoalViewModel
) {
    var title by remember { mutableStateOf("") }
    var target by remember { mutableStateOf("") }
    var deadline by remember { mutableStateOf("") }

    val isValid = title.isNotBlank() && target.toDoubleOrNull() != null

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Add Goal") }
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
                value = deadline,
                onValueChange = { deadline = it },
                label = { Text("Deadline (optional, yyyy-MM-dd)") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    viewModel.addGoal(
                        title = title,
                        target = target.toDouble(),
                        deadline = deadline.ifBlank { null }
                    )
                    navController.popBackStack()
                },
                enabled = isValid,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Goal")
            }
        }
    }
}
