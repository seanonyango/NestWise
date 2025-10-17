package com.example.nestwise.data.model

// Represents a user profile for login and personalization
data class User(
    val id: Int = 0,              // Unique user ID
    val name: String,             // User's display name
    val email: String,            // User's email address
    val password: String          // Plain text password for mock login (hash later)
)
