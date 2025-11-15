package com.example.nestwise.data.repository


import com.example.nestwise.data.dao.TransactionDao
import com.example.nestwise.data.entities.TransactionEntity
import kotlinx.coroutines.flow.Flow

class TransactionRepository(
    private val dao: TransactionDao
) : TransactionRepositoryInterface {

    override val transactions = dao.getAllTransactions()

    override suspend fun add(t: TransactionEntity) = dao.insertTransaction(t)

    override suspend fun update(t: TransactionEntity) = dao.updateTransaction(t)

    override suspend fun delete(id: String) = dao.deleteById(id)

    override suspend fun deleteMultiple(ids: List<String>) = dao.deleteMultiple(ids)
}


