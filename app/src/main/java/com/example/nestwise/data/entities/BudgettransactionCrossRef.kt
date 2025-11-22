package com.example.nestwise.data.entities

import androidx.room.Entity

@Entity(
    primaryKeys = ["budgetId", "transactionId"]
)
data class BudgetTransactionCrossRef(
    val budgetId: String,
    val transactionId: String
)
