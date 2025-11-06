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
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import com.example.nestwise.ui.theme.NestWiseTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.nestwise.ui.navigation.NavRoutes
import com.example.nestwise.ui.screens.AddTransactionScreen
import com.example.nestwise.ui.screens.LoginScreen
import com.example.nestwise.ui.screens.WelcomeScreen
import com.example.nestwise.ui.screens.DashboardScreen
import com.example.nestwise.ui.screens.EditTransactionScreen
import com.example.nestwise.ui.screens.TransactionListScreen


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            NestWiseApp()

        }

    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NestWiseApp() {
    // Navigation controller keeps track of current screen
    val navController = rememberNavController()

    // Host manages all navigation destinations
    NavHost(
        navController = navController,
        startDestination = NavRoutes.Welcome.route
    ) {
        composable(NavRoutes.Welcome.route) {
            WelcomeScreen(
                onLoginClick = { navController.navigate(NavRoutes.Login.route) },
                onSignUpClick = { /* Add sign up later */ }
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
            DashboardScreen(navController = navController)
        }

        composable(NavRoutes.Transactions.route) {
            TransactionListScreen(navController = navController)
        }


        composable("add_transaction") {
            AddTransactionScreen(
                onSaveSuccess = {
                    navController.popBackStack() // return to list after save
                }
            )
        }

        composable(
            route = "edit_transaction/{id}",
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: return@composable
            EditTransactionScreen(
                navController = navController,
                transactionId = id
            )
        }


    }
}


//@Preview(showBackground = true)

