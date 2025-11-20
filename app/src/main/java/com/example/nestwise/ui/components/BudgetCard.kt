package com.example.nestwise.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.nestwise.data.entities.BudgetEntity

@Composable
fun BudgetCard(
    budget: BudgetEntity,
    spent: Double,
    onClick: () -> Unit
) {
    val progress = (spent / budget.limitAmount).coerceIn(0.0, 1.0)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .padding(horizontal = 12.dp),
        onClick = onClick
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                budget.category,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )

            Spacer(Modifier.height(4.dp))

            Text("Limit: \$${budget.limitAmount}")
            Text("Spent: \$${"%.2f".format(spent)}")

            Spacer(Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = progress.toFloat(),
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary
            )

            if (!budget.notes.isNullOrBlank()) {
                Spacer(Modifier.height(8.dp))
                Text(budget.notes ?: "", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
