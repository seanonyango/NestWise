package com.example.nestwise

import GoalViewModelFactory
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.nestwise.di.AppContainer
import com.example.nestwise.notifications.NotificationHelper
import com.example.nestwise.ui.factories.AdviceViewModelFactory
import com.example.nestwise.ui.factories.BudgetViewModelFactory
import com.example.nestwise.ui.factories.TransactionViewModelFactory
import com.example.nestwise.ui.navigation.NavRoutes
import com.example.nestwise.ui.screens.AddBudgetScreen
import com.example.nestwise.ui.screens.AddGoalScreen
import com.example.nestwise.ui.screens.AddTransactionScreen
import com.example.nestwise.ui.screens.BudgetListScreen
import com.example.nestwise.ui.screens.DashboardScreen
import com.example.nestwise.ui.screens.EditBudgetScreen
import com.example.nestwise.ui.screens.EditGoalScreen
import com.example.nestwise.ui.screens.EditTransactionScreen
import com.example.nestwise.ui.screens.GoalsListScreen
import com.example.nestwise.ui.screens.ImportCsvScreen
import com.example.nestwise.ui.screens.LoginScreen
import com.example.nestwise.ui.screens.ProfileScreen
import com.example.nestwise.ui.screens.SignUpScreen
import com.example.nestwise.ui.screens.TransactionListScreen
import com.example.nestwise.ui.screens.WelcomeScreen
import com.example.nestwise.viewmodel.AdviceViewModel
import com.example.nestwise.viewmodel.BudgetViewModel
import com.example.nestwise.viewmodel.GoalViewModel
import com.example.nestwise.viewmodel.TransactionViewModel
import com.example.nestwise.viewmodel.UserViewModel
import com.example.nestwise.viewmodel.UserViewModelFactory
import com.example.nestwise.work.DailyTipWorker
import java.util.concurrent.TimeUnit

val LocalAppContainer = staticCompositionLocalOf<AppContainer> {
    error("AppContainer not found!")
}

class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appContainer = AppContainer(applicationContext)
        NotificationHelper.createChannel(this)

        setupDailyTipWorker()

        setContent {

            CompositionLocalProvider(
                LocalAppContainer provides appContainer
            ) {
//                NestWiseTheme {

                    val navController = rememberNavController()

                    val transactionViewModel: TransactionViewModel = viewModel(
                        factory = TransactionViewModelFactory(appContainer.transactionRepository)
                    )

                    val budgetViewModel: BudgetViewModel = viewModel(
                        factory = BudgetViewModelFactory(appContainer.budgetRepository)
                    )

                    val goalViewModel: GoalViewModel = viewModel(
                        factory = GoalViewModelFactory(appContainer.goalRepository)
                    )
                    val adviceViewModel: AdviceViewModel = viewModel(
                        factory = AdviceViewModelFactory(appContainer.adviceRepository)
                    )

                    val userViewModel: UserViewModel = viewModel(
                        factory = UserViewModelFactory(appContainer.userRepository)
                    )

                    NavHost(
                        navController = navController,
                        startDestination = NavRoutes.Welcome.route
                    ) {

                        composable(NavRoutes.Welcome.route) {
                            WelcomeScreen(
                                onLoginClick = { navController.navigate(NavRoutes.Login.route) },
                                onSignUpClick = { navController.navigate(NavRoutes.SignUp.route) }
                            )
                        }

                        composable(NavRoutes.Login.route) {
                            LoginScreen(
                                navController = navController,
                                userViewModel = userViewModel,
                                onBackClick = { navController.popBackStack() },
                                onLoginClick = {
                                    navController.navigate(NavRoutes.Dashboard.route) {
                                        popUpTo(NavRoutes.Welcome.route) { inclusive = true }
                                    }
                                },
                                onForgotPasswordClick = {},
                                onSignUpClick = {
                                    navController.navigate(NavRoutes.SignUp.route)
                                }
                            )
                        }

                        composable(NavRoutes.SignUp.route) {
                            SignUpScreen(
                                onBackToLogin = { navController.popBackStack() },
                                onSignUpSuccess = {
                                    navController.navigate(NavRoutes.Login.route) {
                                        popUpTo(NavRoutes.Welcome.route) { inclusive = false }
                                    }
                                },
                                userViewModel = userViewModel
                            )
                        }




                        composable(NavRoutes.Dashboard.route) {
                            DashboardScreen(
                                navController = navController,
                                transactionVM = transactionViewModel,
                                goalVM = goalViewModel,
                                adviceVM = adviceViewModel,
                                userViewModel = userViewModel
                            )
                        }

                        composable(NavRoutes.Profile.route) {
                            ProfileScreen(
                                navController = navController,
                                userViewModel = userViewModel,
                                onLogout = {
                                    navController.navigate(NavRoutes.Welcome.route) {
                                        popUpTo(NavRoutes.Dashboard.route) { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable(NavRoutes.Transactions.route) {
                            TransactionListScreen(
                                navController = navController,
                                transactionViewModel = transactionViewModel,
                                budgetViewModel = budgetViewModel
                            )
                        }

                        composable("add_transaction") {
                            AddTransactionScreen(
                                viewModel = transactionViewModel,
                                navController = navController,
                                onSaveSuccess = { navController.popBackStack() }
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

                        composable(NavRoutes.ImportCsv.route) {
                            ImportCsvScreen(
                                navController = navController,
                                viewModel = transactionViewModel
                            )
                        }

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
//                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.GINGERBREAD)
    private fun setupDailyTipWorker() {
        val request = PeriodicWorkRequestBuilder<DailyTipWorker>(
            1, TimeUnit.DAYS
        ).build()

        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(
                "daily_tip_work",
                ExistingPeriodicWorkPolicy.UPDATE,
                request
            )
    }
}
