package com.example.nestwise.data.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.nestwise.data.database.AppDatabase
import com.example.nestwise.data.entities.TransactionEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest=Config.NONE)
class TransactionDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var dao: TransactionDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.transactionDao()
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun insertTransaction_savesSuccessfully() = runBlocking {
        val sample = TransactionEntity(
            id = "1",
            title = "Coffee",
            amount = 5.0,
            category = "Food",
            date = "2025-01-01",
            type = "EXPENSE"
        )

        dao.insertTransaction(sample)

        val list = dao.getAllTransactions().first()
        assertEquals(1, list.size)
    }

    @Test
    fun deleteTransaction_worksCorrectly() = runBlocking {
        val sample = TransactionEntity(
            id = "1",
            title = "Lunch",
            amount = 12.0,
            category = "Food",
            date = "2025-01-01",
            type = "EXPENSE"
        )

        dao.insertTransaction(sample)
        dao.deleteById("1")

        val result = dao.getAllTransactions().first()
        assertTrue(result.isEmpty())
    }
}
