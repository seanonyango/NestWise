package com.example.nestwise.data.repository

import TransactionDao
import com.example.nestwise.data.entities.TransactionEntity
import kotlinx.coroutines.flow.Flow

class TransactionRepository(private val dao: TransactionDao) {

    val transactions: Flow<List<TransactionEntity>> = dao.getAllTransactions()

    suspend fun add(t: TransactionEntity) = dao.insertTransaction(t)
    suspend fun update(t: TransactionEntity) = dao.updateTransaction(t)
    suspend fun delete(t: TransactionEntity) = dao.deleteTransaction(t)
    suspend fun deleteMultiple(ids: List<String>) = dao.deleteMultiple(ids)
}
