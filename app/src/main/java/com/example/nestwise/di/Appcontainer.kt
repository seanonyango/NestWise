package com.example.nestwise.di

import android.content.Context
import com.example.nestwise.data.database.AppDatabase
import com.example.nestwise.data.repository.TransactionRepository
import com.example.nestwise.data.repository.BudgetRepository
import com.example.nestwise.data.repository.GoalRepository

// GoalRepository later

class AppContainer(context: Context) {

    // Create ONE shared Room database
    private val database: AppDatabase = AppDatabase.getInstance(context)

    // Repositories (shared across app)
    val transactionRepository: TransactionRepository =
        TransactionRepository(database.transactionDao())

    val budgetRepository: BudgetRepository =
        BudgetRepository(database.budgetDao())


    val goalRepository: GoalRepository =
        GoalRepository(database.goalDao())

}
