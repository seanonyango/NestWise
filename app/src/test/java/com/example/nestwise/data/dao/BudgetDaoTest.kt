package com.example.nestwise.data.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.nestwise.data.database.AppDatabase
import com.example.nestwise.data.entities.BudgetEntity
import junit.framework.TestCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BudgetDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var dao: BudgetDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        dao = db.budgetDao()
    }

    @Test
    fun insert_budget_successfully() = runBlocking {
        val budget = BudgetEntity(category = "Food", limitAmount = 200.0)
        dao.insertBudget(budget)

        val list = dao.getAllBudgets().first()
        TestCase.assertEquals(1, list.size)
    }
}