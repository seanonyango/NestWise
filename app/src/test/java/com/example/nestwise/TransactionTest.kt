package com.example.nestwise

import com.example.nestwise.data.model.Transaction
import org.junit.Assert.assertEquals
import org.junit.Test

class TransactionTest {

    @Test
    fun testTransactionData() {
        val transaction = Transaction(
            title = "Groceries",
            amount = 50.0,
            category = "Food",
            type = "Expense",
            date = "2025-10-18"
        )

        // Check that the data was stored correctly
        assertEquals("Groceries", transaction.title)
        assertEquals(50.0, transaction.amount, 0.0)
        assertEquals("Food", transaction.category)
        assertEquals("Expense", transaction.type)
    }
}
