package com.example.nestwise.data

import java.util.UUID
import java.time.LocalDate


data class Transaction(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val amount: Double,
    val category: String,
    val date: LocalDate,
    val type: TransactionType // INCOME or EXPENSE
)

enum class TransactionType { INCOME, EXPENSE }