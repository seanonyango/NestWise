package com.example.nestwise

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
import com.example.nestwise.data.repository.TransactionRepository
import com.example.nestwise.ui.factories.TransactionViewModelFactory
import com.example.nestwise.ui.navigation.NavRoutes
import com.example.nestwise.ui.screens.AddTransactionScreen
import com.example.nestwise.ui.screens.LoginScreen
import com.example.nestwise.ui.screens.WelcomeScreen
import com.example.nestwise.ui.screens.DashboardScreen
import com.example.nestwise.ui.screens.EditTransactionScreen
import com.example.nestwise.ui.screens.ImportCsvScreen
import com.example.nestwise.ui.screens.TransactionListScreen
import com.example.nestwise.viewmodel.TransactionViewModel


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            // ------------------------------------------------
            // ⬇️ THIS IS A COMPOSABLE CONTEXT
            // ------------------------------------------------

            val navController = rememberNavController()

            // Provide ViewModel dependencies INSIDE the composable
            val context = LocalContext.current
            val db = AppDatabase.getInstance(context)
            val repo = TransactionRepository(db.transactionDao())

            val transactionViewModel: TransactionViewModel = viewModel(
                factory = TransactionViewModelFactory(repo)
            )

            // ------------------------------------------------
            // NOW BUILD NAVHOST WITH THE VIEWMODEL
            // ------------------------------------------------
            NavHost(
                navController = navController,
                startDestination = NavRoutes.Welcome.route
            ) {

                composable(NavRoutes.Welcome.route) {
                    WelcomeScreen(
                        onLoginClick = { navController.navigate(NavRoutes.Login.route) },
                        onSignUpClick = {}
                    )
                }

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

                composable(NavRoutes.Dashboard.route) {
                    DashboardScreen(
                        navController = navController,
                        viewModel = transactionViewModel
                    )
                }

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

                composable(NavRoutes.ImportCsv.route) {
                    ImportCsvScreen(
                        navController = navController,
                        viewModel = transactionViewModel
                    )
                }
            }
        }
    }
}



//@Preview(showBackground = true)

