package com.example.nestwise.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.nestwise.ui.components.BottomNavBar
import com.example.nestwise.viewmodel.TransactionViewModel


@Composable
fun DashboardScreen(
    navController: NavController,
    viewModel: TransactionViewModel
) {

    // --- App Colors ---
    val primaryBlue = Color(0xFF1565C0)
    val accentOrange = Color(0xFFFFA726) // Complementary accent for visuals
    val backgroundWhite = Color.White

    // --- Dummy Data (temporary placeholders for UI design) ---
    val monthlyIncome = "$5,000"
    val monthlySpending = "$2,300"
    val savingsProgress = 0.45f // 45%
    val topCategory = "Food & Dining"
    val topCategorySpending = "$650"

    // --- Bottom Navigation Items ---
    val navItems = listOf("Home", "Transactions", "Budgets", "Goals")

    Scaffold(
        containerColor = backgroundWhite,
        // --- Bottom navigation bar ---
        bottomBar = {BottomNavBar(navController)
        },
    ) { innerPadding ->

        // --- Main Dashboard Content ---
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // --- Header Section ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🪺 NestWise",
                    color = primaryBlue,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )

                IconButton(onClick = { /* Notifications or profile later */ }) {
                    Icon(Icons.Default.Person, contentDescription = "Notifications", tint = primaryBlue)
                }
            }

            // --- Monthly Income Card ---
            DashboardCard(
                title = "Monthly Income",
                value = monthlyIncome,
                color = primaryBlue
            )

            Spacer(modifier = Modifier.height(12.dp))

            // --- Monthly Spending Card ---
            DashboardCard(
                title = "Monthly Spending",
                value = monthlySpending,
                color = accentOrange
            )

            Spacer(modifier = Modifier.height(12.dp))

            // --- Savings Progress Card ---
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Text("Savings Progress", color = primaryBlue, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                    progress = { savingsProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp),
                    color = primaryBlue,
                    trackColor = Color(0xFFBBDEFB),
                    strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${(savingsProgress * 100).toInt()}% of goal achieved",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // --- Top Spending Category Card ---
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFBE9E7)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Text("Top Spending Category", color = accentOrange, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "$topCategory - $topCategorySpending",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- Footer Text ---
            Text(
                text = "Dummy data for demonstration. to be reviewed later",
                color = Color.Gray,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

// --- Reusable Dashboard Card Component ---
@Composable
fun DashboardCard(title: String, value: String, color: Color) {
    Card(
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f)),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(title, color = color, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, color = color, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun DashboardScreenPreview() {
//    val navController = rememberNavController()
//    DashboardScreen(navController = navController)
//}
