package com.example.nestwise.data.repository

import com.example.nestwise.data.entities.TransactionEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FakeTransactionRepository : TransactionRepositoryInterface {

    private val _transactions = MutableStateFlow<List<TransactionEntity>>(emptyList())
    override val transactions: StateFlow<List<TransactionEntity>> = _transactions

    override suspend fun add(t: TransactionEntity) {
        _transactions.value = _transactions.value + t
    }

    override suspend fun update(t: TransactionEntity) {
        _transactions.value = _transactions.value.map { old ->
            if (old.id == t.id) t else old
        }
    }

    override suspend fun delete(id: String) {
        _transactions.value = _transactions.value.filterNot { it.id == id }
    }

    override suspend fun deleteMultiple(ids: List<String>) {
        _transactions.value = _transactions.value.filterNot { it.id in ids }
    }
}
