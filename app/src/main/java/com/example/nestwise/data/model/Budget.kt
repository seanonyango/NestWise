package com.example.nestwise.data.model

// Represents a user's monthly budget for a specific category
data class Budget(
    val id: Int = 0,               // (auto-generated later by Room)
    val category: String,          // Budget category (e.g., "Food", "Transport")
    val limit: Double,             // The spending limit the user sets for this category
    val spent: Double = 0.0,       // How much has been spent so far (can be updated dynamically)
    val month: String
)
