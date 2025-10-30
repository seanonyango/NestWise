package com.example.nestwise.ui.navigation

sealed class NavRoutes(val route: String) {
    object Welcome : NavRoutes("welcome")
    object Login : NavRoutes("login")
    object Dashboard : NavRoutes("dashboard")
    object Transactions : NavRoutes("transactions")
    object Budgets : NavRoutes("budgets")
    object Goals : NavRoutes("goals")
}
