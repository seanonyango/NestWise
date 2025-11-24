package com.example.nestwise.data.database

import com.example.nestwise.data.dao.BudgetDao
import com.example.nestwise.data.dao.TransactionDao   // if DAO is placed in /data/database
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.nestwise.data.dao.DailyTipDao
import com.example.nestwise.data.dao.GoalDao
import com.example.nestwise.data.entities.BudgetEntity
import com.example.nestwise.data.entities.BudgetTransactionCrossRef
import com.example.nestwise.data.entities.DailyTipEntity
import com.example.nestwise.data.entities.GoalEntity
import com.example.nestwise.data.entities.TransactionEntity

@Database(
    entities = [
        TransactionEntity::class,
        BudgetEntity::class,
        BudgetTransactionCrossRef::class,
        GoalEntity::class,
        DailyTipEntity::class
    ],
    version = 7,
    exportSchema = false
)



abstract class AppDatabase : RoomDatabase() {

    abstract fun transactionDao(): TransactionDao
    abstract fun budgetDao(): BudgetDao
    abstract fun goalDao(): GoalDao
    abstract fun dailyTipDao(): DailyTipDao


    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "nestwise_db"   // <-- database filename
                ).fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}

