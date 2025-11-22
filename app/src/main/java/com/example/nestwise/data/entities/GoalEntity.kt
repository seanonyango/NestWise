package com.example.nestwise.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "goals")
data class GoalEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val title: String,
    val targetAmount: Double,
    val currentAmount: Double = 0.0,
    val deadline: String? = null   // yyyy-MM-dd
)
