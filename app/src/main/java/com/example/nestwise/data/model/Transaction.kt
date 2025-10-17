package com.example.nestwise.data.model

// Represents a single transaction record such as an expense or income
data class Transaction(
    val id: Int = 0,
    val title: String,             // Short description
    val amount: Double,
    val category: String,          // Category such as "Food", "Transport", "Bills"
    val type: String,              // Either "Expense" or "Income" to identify transaction type
    val date: String,              // Date of the transaction
    val notes: String? = null      // Optional field for extra notes or details
)
