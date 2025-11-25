package com.example.nestwise.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
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
import com.example.nestwise.data.TransactionType
import com.example.nestwise.ui.components.BottomNavBar
import com.example.nestwise.ui.components.NotificationPermissionCard
import com.example.nestwise.utils.formatCurrency
import com.example.nestwise.viewmodel.AdviceViewModel
import com.example.nestwise.viewmodel.GoalViewModel
import com.example.nestwise.viewmodel.TransactionViewModel
import com.example.nestwise.viewmodel.UserViewModel
import java.time.LocalDate


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DashboardScreen(
    navController: NavController,
    transactionVM: TransactionViewModel,
    goalVM: GoalViewModel,
    adviceVM: AdviceViewModel,
    userViewModel: UserViewModel
) {
    val primaryBlue = Color(0xFF1565C0)
    val accentOrange = Color(0xFFFFA726)
    val backgroundWhite = Color.White

    // ---- REAL DATA ----
    val transactions by transactionVM.transactions.collectAsState()
    val goals by goalVM.goals.collectAsState()
    val latestTipState by adviceVM.latestTip.collectAsState()
    val latestTipText = latestTipState?.advice ?: "Tap refresh to get today’s tip."
    val user by userViewModel.currentUser.collectAsState()
    val currency = user?.currency ?: "AUD"
    val displayName = user?.name ?: "There"


    // ---- MONTHLY INCOME ----
    val monthlyIncomeAmount = transactions
        .filter { it.type == TransactionType.INCOME && it.date.isThisMonth() }
        .sumOf { it.amount }

    val monthlyIncome = formatCurrency(monthlyIncomeAmount, currency)

    // ---- MONTHLY SPENDING ----
    val monthlySpendingAmount = transactions
        .filter { it.type == TransactionType.EXPENSE && it.date.isThisMonth() }
        .sumOf { it.amount }

    val monthlySpending = formatCurrency(monthlyIncomeAmount, currency)

    // ---- TOP CATEGORY ----
    val topCategoryEntry = transactions
        .filter { it.type == TransactionType.EXPENSE }
        .groupBy { it.category }
        .maxByOrNull { (_, items) -> items.sumOf { it.amount } }

    val topCategory = topCategoryEntry?.key ?: "None"
    val topCategorySpending =
        "₹${"%,.2f".format(topCategoryEntry?.value?.sumOf { it.amount } ?: 0.0)}"

    // ---- GOAL PROGRESS ----
    val totalSaved = goals.sumOf { it.currentAmount }
    val totalTarget = goals.sumOf { it.targetAmount }

    val savingsProgress =
        if (totalTarget > 0) (totalSaved / totalTarget).coerceIn(0.0, 1.0).toFloat()
        else 0f

    Scaffold(
        containerColor = backgroundWhite,
        bottomBar = { BottomNavBar(navController) },
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    "🪺 NestWise",
                    color = primaryBlue,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Hey, $displayName 👋",
                    color = primaryBlue,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )


                IconButton(
                    onClick = { navController.navigate("profile") }
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = "Profile",
                        tint = primaryBlue
                    )
                }

            }

            NotificationPermissionCard()
            Spacer(Modifier.height(12.dp))


            // Income card
            DashboardCard("Monthly Income", monthlyIncome, primaryBlue)
            Spacer(Modifier.height(12.dp))

            // Spending card
            DashboardCard("Monthly Spending", monthlySpending, accentOrange)
            Spacer(Modifier.height(12.dp))

            // Savings goal progress
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        "Savings Progress",
                        color = primaryBlue,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = savingsProgress,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp),
                        color = primaryBlue,
                        trackColor = Color(0xFFBBDEFB)
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "${(savingsProgress * 100).toInt()}% of goals achieved",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            // Top Category Card
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFBE9E7)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        "Top Spending Category",
                        color = accentOrange,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "$topCategory - $topCategorySpending",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        "Today’s Tip",
                        color = Color(0xFF2E7D32),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = latestTipText,
                        color = Color.DarkGray,
                        fontSize = 14.sp
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedButton(
                        onClick = { adviceVM.refreshTip() },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Refresh Tip")
                    }
                }
            }

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

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.isThisMonth(): Boolean {
    val now = LocalDate.now()
    return this.year == now.year && this.month == now.month
}

