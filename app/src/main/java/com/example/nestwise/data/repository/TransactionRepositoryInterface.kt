package com.example.nestwise.data.repository

import com.example.nestwise.data.entities.TransactionEntity
import kotlinx.coroutines.flow.Flow

interface TransactionRepositoryInterface {

    val transactions: Flow<List<TransactionEntity>>

    suspend fun add(t: TransactionEntity)

    suspend fun update(t: TransactionEntity)

    suspend fun delete(id: String)

    suspend fun deleteMultiple(ids: List<String>)
}
