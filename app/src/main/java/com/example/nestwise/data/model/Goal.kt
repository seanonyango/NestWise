package com.example.nestwise.data.model

// Represents a savings goal that the user is working towards
data class Goal(
    val id: Int = 0,                    // Unique ID(auto generated)
    val title: String,                  // Goal name
    val targetAmount: Double,           // Total amount the user wants to save
    val currentAmount: Double = 0.0,    // Current progress towards the goal(dynamic)
    val deadline: String                // Deadline intended to finish saving
)
