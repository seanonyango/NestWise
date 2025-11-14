package com.example.nestwise.viewmodel

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.viewModelScope
import com.example.nestwise.data.Transaction
import com.example.nestwise.data.entities.TransactionEntity
import com.example.nestwise.data.repository.TransactionRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

//class TransactionViewModel : ViewModel() {
//
//    // temporary in-memory list
//    var transactions = mutableStateListOf<Transaction>()
//        private set
//
//    fun addTransaction(t: Transaction) {
//        transactions.add(t)
//    }
//
//    fun updateTransaction(updated: Transaction) {
//        val index = transactions.indexOfFirst { it.id == updated.id }
//        if (index != -1) transactions[index] = updated
//    }
//
//    fun deleteTransaction(id: String) {
//        transactions.removeAll { it.id == id }
//    }
//
//    fun deleteMultiple(ids: List<String>) {
//        transactions.removeAll { it.id in ids }
//    }
//}

class TransactionViewModel(
    private val repo: TransactionRepository
) : ViewModel() {

    val transactions = repo.transactions
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun addTransaction(t: TransactionEntity) = viewModelScope.launch {
        repo.add(t)
    }
}
