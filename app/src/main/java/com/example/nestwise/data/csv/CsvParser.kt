package com.example.nestwise.data.csv

import com.example.nestwise.data.entities.TransactionEntity
import java.util.UUID

object CsvParser {

    fun parseRow(row: String): TransactionEntity? {
        val parts = row.split(",")

        if (parts.size != 5) return null

        val title = parts[0].trim()
        val amount = parts[1].trim().toDoubleOrNull() ?: return null
        val category = parts[2].trim()
        val date = parts[3].trim()   // Must be yyyy-MM-dd
        val type = parts[4].trim().uppercase()

        if (type !in listOf("INCOME", "EXPENSE")) return null

        return TransactionEntity(
            id = UUID.randomUUID().toString(),
            title = title,
            amount = amount,
            category = category,
            date = date,
            type = type
        )
    }
}
