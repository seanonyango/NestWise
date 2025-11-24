package com.example.nestwise.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "daily_tips")
data class DailyTipEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val advice: String,
    val fetchedAt: String,     // e.g. "2025-11-20"
    val source: String = "AdviceSlip"
)
