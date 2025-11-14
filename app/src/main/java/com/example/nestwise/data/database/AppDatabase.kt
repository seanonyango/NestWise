package com.example.nestwise.data.database

import TransactionDao
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.nestwise.data.entities.TransactionEntity

@Database(entities = [TransactionEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
}
