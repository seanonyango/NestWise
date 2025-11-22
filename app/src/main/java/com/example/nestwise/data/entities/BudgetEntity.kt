package com.example.nestwise.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "budgets")
data class BudgetEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),

    val category: String,

    val limitAmount: Double,

    // NEW — amount already spent, default 0
    val spentAmount: Double = 0.0,

    // Optional notes
    val notes: String? = null
)
