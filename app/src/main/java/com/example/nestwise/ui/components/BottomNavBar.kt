package com.example.nestwise.ui.components

import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.nestwise.ui.navigation.NavRoutes

@Composable
fun BottomNavBar(navController: NavController) {
    val items = listOf(
        NavRoutes.Dashboard,
        NavRoutes.Transactions,
        NavRoutes.Budgets,
        NavRoutes.Goals
    )

    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    NavigationBar(containerColor = Color(0xFF1565C0)) {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(NavRoutes.Dashboard.route)
                            launchSingleTop = true
                        }
                    }
                },
                icon = {
                    when (item.route) {
                        NavRoutes.Dashboard.route -> Icon(Icons.Default.Home, contentDescription = "Home", tint = Color.White)
                        NavRoutes.Transactions.route -> Icon(Icons.Default.List, contentDescription = "Transactions", tint = Color.White)
                        NavRoutes.Budgets.route -> Icon(Icons.Default.AttachMoney, contentDescription = "Budgets", tint = Color.White)
                        NavRoutes.Goals.route -> Icon(Icons.Default.Flag, contentDescription = "Goals", tint = Color.White)
                    }
                },
                label = { Text(item.route.replaceFirstChar { it.uppercase() }, color = Color.White) },
                alwaysShowLabel = true
            )
        }
    }
}
