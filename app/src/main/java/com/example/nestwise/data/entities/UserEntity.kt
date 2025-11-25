package com.example.nestwise.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val name: String,
    val email: String,
    val password: String,      // plain text for now (local/demo only)
    val currency: String       // e.g. "AUD", "USD"
)
