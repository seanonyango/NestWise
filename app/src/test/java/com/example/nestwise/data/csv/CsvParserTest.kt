package com.example.nestwise.data.csv

import com.example.nestwise.data.csv.CsvParser.parseRow
import org.junit.Assert.*
import org.junit.Test

class CsvParserTest {

    @Test
    fun validRow_parsesSuccessfully() {
        val row = "Groceries,50.25,Food,2025-01-15,EXPENSE"

        val entity = parseRow(row)

        assertNotNull(entity)
        assertEquals("Groceries", entity!!.title)
        assertEquals(50.25, entity.amount, 0.001)
        assertEquals("Food", entity.category)
        assertEquals("2025-01-15", entity.date)
        assertEquals("EXPENSE", entity.type)
    }

    @Test
    fun invalidAmount_returnsNull() {
        val row = "Coffee,abc,Food,2025-01-15,EXPENSE"

        val entity = parseRow(row)

        assertNull(entity)
    }

    @Test
    fun missingColumns_returnsNull() {
        val row = "Only,Two,Fields"

        val entity = parseRow(row)

        assertNull(entity)
    }

    @Test
    fun invalidType_returnsNull() {
        val row = "Groceries,50.00,Food,2025-01-15,INVALIDTYPE"

        val entity = parseRow(row)

        assertNull(entity)
    }

    @Test
    fun trimsWhitespace_correctly() {
        val row = " Groceries , 50.0 , Food , 2025-01-15 , income "

        val entity = parseRow(row)

        assertNotNull(entity)
        assertEquals("Groceries", entity!!.title)
        assertEquals("INCOME", entity.type) // uppercase conversion confirmed
    }
}
