package com.example.nestwise

import GoalViewModelFactory
import android.R.style.Theme
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import com.example.nestwise.ui.theme.NestWiseTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.nestwise.data.database.AppDatabase
import com.example.nestwise.data.repository.BudgetRepository
import com.example.nestwise.data.repository.TransactionRepository
import com.example.nestwise.ui.factories.BudgetViewModelFactory
import com.example.nestwise.ui.factories.TransactionViewModelFactory
import com.example.nestwise.ui.navigation.NavRoutes
import com.example.nestwise.ui.screens.AddBudgetScreen
import com.example.nestwise.ui.screens.AddTransactionScreen
import com.example.nestwise.ui.screens.BudgetListScreen
import com.example.nestwise.ui.screens.LoginScreen
import com.example.nestwise.ui.screens.WelcomeScreen
import com.example.nestwise.ui.screens.DashboardScreen
import com.example.nestwise.ui.screens.EditBudgetScreen
import com.example.nestwise.ui.screens.EditTransactionScreen
import com.example.nestwise.ui.screens.ImportCsvScreen
import com.example.nestwise.ui.screens.TransactionListScreen
import com.example.nestwise.viewmodel.BudgetViewModel
import com.example.nestwise.viewmodel.TransactionViewModel

import androidx.compose.runtime.staticCompositionLocalOf
import com.example.nestwise.di.AppContainer
import com.example.nestwise.ui.screens.AddGoalScreen
import com.example.nestwise.ui.screens.EditGoalScreen
import com.example.nestwise.ui.screens.GoalsListScreen
import com.example.nestwise.viewmodel.GoalViewModel

val LocalAppContainer = staticCompositionLocalOf<AppContainer> {
    error("AppContainer not found!")
}


class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create the DI container ONCE for the whole app
        val appContainer = AppContainer(applicationContext)

        setContent {

            // Provide DI container to the whole app tree
            CompositionLocalProvider(
                LocalAppContainer provides appContainer
            ) {
                NestWiseTheme {

                    val navController = rememberNavController()

                    // --- Retrieve repositories FROM DI ---
                    val transactionRepo = appContainer.transactionRepository
                    val budgetRepo = appContainer.budgetRepository

                    // --- Create ViewModels FROM DI repositories ---
                    val transactionViewModel: TransactionViewModel = viewModel(
                        factory = TransactionViewModelFactory(transactionRepo)
                    )

                    val budgetViewModel: BudgetViewModel = viewModel(
                        factory = BudgetViewModelFactory(budgetRepo)
                    )

                    val goalViewModel: GoalViewModel = viewModel(
                        factory = GoalViewModelFactory(appContainer.goalRepository)
                    )






                    // ------------------- NAV HOST ---------------------

                    NavHost(
                        navController = navController,
                        startDestination = NavRoutes.Welcome.route
                    ) {

                        // ---------- Welcome ----------
                        composable(NavRoutes.Welcome.route) {
                            WelcomeScreen(
                                onLoginClick = { navController.navigate(NavRoutes.Login.route) },
                                onSignUpClick = {}
                            )
                        }

                        // ---------- Login ----------
                        composable(NavRoutes.Login.route) {
                            LoginScreen(
                                onBackClick = { navController.popBackStack() },
                                onLoginClick = {
                                    navController.navigate(NavRoutes.Dashboard.route) {
                                        popUpTo(NavRoutes.Welcome.route) { inclusive = true }
                                    }
                                },
                                onForgotPasswordClick = {},
                                onSignUpClick = {}
                            )
                        }

                        // ---------- Dashboard ----------
                        composable(NavRoutes.Dashboard.route) {
                            DashboardScreen(
                                navController = navController,
                                viewModel = transactionViewModel
                            )
                        }

                        // ---------- Transactions ----------
                        composable(NavRoutes.Transactions.route) {
                            TransactionListScreen(
                                navController = navController,
                                viewModel = transactionViewModel
                            )
                        }

                        composable("add_transaction") {
                            AddTransactionScreen(
                                viewModel = transactionViewModel,
                                onSaveSuccess = {
                                    navController.popBackStack()
                                }
                            )
                        }

                        composable(
                            route = "edit_transaction/{id}",
                            arguments = listOf(navArgument("id") { type = NavType.StringType })
                        ) { entry ->
                            val id = entry.arguments?.getString("id")!!
                            EditTransactionScreen(
                                navController = navController,
                                transactionId = id,
                                viewModel = transactionViewModel
                            )
                        }

                        // ---------- CSV Import ----------
                        composable(NavRoutes.ImportCsv.route) {
                            ImportCsvScreen(
                                navController = navController,
                                viewModel = transactionViewModel
                            )
                        }

                        // ---------- Budgets ----------
                        composable("budgets") {
                            BudgetListScreen(navController, budgetViewModel)
                        }

                        composable("add_budget") {
                            AddBudgetScreen(navController, budgetViewModel)
                        }

                        composable("edit_budget/{id}") { backStack ->
                            val id = backStack.arguments?.getString("id")!!
                            EditBudgetScreen(navController, id, budgetViewModel)
                        }

                        composable(NavRoutes.Goals.route) {
                            GoalsListScreen(navController, goalViewModel)
                        }

                        composable("add_goal") {
                            AddGoalScreen(navController, goalViewModel)
                        }

                        composable("edit_goal/{id}") { backStack ->
                            val id = backStack.arguments?.getString("id")!!
                            EditGoalScreen(navController, id, goalViewModel)
                        }

                    }
                }
            }
        }
    }
}

