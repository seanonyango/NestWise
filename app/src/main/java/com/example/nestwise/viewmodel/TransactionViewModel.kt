package com.example.nestwise.viewmodel

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateListOf
import com.example.nestwise.data.Transaction

class TransactionViewModel : ViewModel() {

    // temporary in-memory list
    var transactions = mutableStateListOf<Transaction>()
        private set

    fun addTransaction(t: Transaction) {
        transactions.add(t)
    }

    fun updateTransaction(updated: Transaction) {
        val index = transactions.indexOfFirst { it.id == updated.id }
        if (index != -1) transactions[index] = updated
    }

    fun deleteTransaction(id: String) {
        transactions.removeAll { it.id == id }
    }

    fun deleteMultiple(ids: List<String>) {
        transactions.removeAll { it.id in ids }
    }
}
