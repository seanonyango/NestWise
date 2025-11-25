package com.example.nestwise.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.nestwise.data.entities.GoalEntity
import com.example.nestwise.ui.components.BottomNavBar
import com.example.nestwise.viewmodel.GoalViewModel

@Composable
fun GoalsListScreen(
    navController: NavController,
    viewModel: GoalViewModel
) {
    val goals by viewModel.goals.collectAsState()
    val primaryBlue = Color(0xFF1565C0)

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = { BottomNavBar(navController) },
        floatingActionButton = {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
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
                "🪺 NestWise",
                color = primaryBlue,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )

            // Title section
            Text(
                text = "Savings Goals",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.height(16.dp))

            // Empty state
            if (goals.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No savings goals yet. Tap + to add one.",
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                    )
                }
            }
            else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(goals.size) { index ->
                        val goal = goals[index]

                        GoalCard(
                            goal = goal,
                            onClick = { navController.navigate("edit_goal/${goal.id}") }
                        )
                    }
                }
            }
        }
    }
}




// ---------------------------------------------------------
// GOAL CARD (Theme-Styled)
// ---------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalCard(
    goal: GoalEntity,
    onClick: () -> Unit
) {
    val progress = (goal.currentAmount / goal.targetAmount)
        .coerceIn(0.0, 1.0)

    val primary = MaterialTheme.colorScheme.primary
    val secondary = MaterialTheme.colorScheme.secondary
    val surface = MaterialTheme.colorScheme.surface
    val onSurface = MaterialTheme.colorScheme.onSurface

    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = surface),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(
            Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {

            // 🔷 Title + Icon Row
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    goal.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = primary
                )

                Icon(
                    Icons.Default.Flag,
                    contentDescription = null,
                    tint = secondary
                )
            }

            Spacer(Modifier.height(6.dp))

            // 🔸 Saved & Target Text
            Text(
                "Saved: \$${"%.2f".format(goal.currentAmount)} / \$${goal.targetAmount}",
                style = MaterialTheme.typography.bodyMedium,
                color = onSurface.copy(alpha = 0.9f)
            )

            Spacer(Modifier.height(10.dp))

            // 📊 Progress Bar
            LinearProgressIndicator(
                progress = progress.toFloat(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp),
                trackColor = primary.copy(alpha = 0.1f),
                color = primary,
                strokeCap = ProgressIndicatorDefaults.LinearStrokeCap
            )

            Spacer(Modifier.height(6.dp))

            // 🕒 Optional Deadline
            if (!goal.deadline.isNullOrEmpty()) {
                Text(
                    "Deadline: ${goal.deadline}",
                    style = MaterialTheme.typography.bodySmall,
                    color = onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}
