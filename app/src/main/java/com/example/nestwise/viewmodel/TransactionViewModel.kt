package com.example.nestwise.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nestwise.data.Transaction
import com.example.nestwise.data.entities.TransactionEntity
import com.example.nestwise.data.mappers.toEntity
import com.example.nestwise.data.mappers.toModel
import com.example.nestwise.data.repository.FakeTransactionRepository
import com.example.nestwise.data.repository.TransactionRepository
import com.example.nestwise.data.repository.TransactionRepositoryInterface
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

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
    private val repo: TransactionRepositoryInterface
) : ViewModel() {


    @RequiresApi(Build.VERSION_CODES.O)
    val transactions = repo.transactions
        .map { list -> list.map { it.toModel() } }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())


    fun addTransaction(t: Transaction) = viewModelScope.launch {
        repo.add(t.toEntity())
    }

    fun importTransactions(list: List<TransactionEntity>) = viewModelScope.launch {
        list.forEach { repo.add(it) }
    }

    fun updateTransaction(t: Transaction) = viewModelScope.launch {
        repo.update(t.toEntity())
    }

    fun deleteTransaction(id: String) = viewModelScope.launch {
        repo.delete(id)
    }

    fun deleteMultiple(ids: List<String>) = viewModelScope.launch {
        repo.deleteMultiple(ids)
    }

    companion object {
        fun preview(): TransactionViewModel {
            val fakeRepo = FakeTransactionRepository()
            val vm = TransactionViewModel(fakeRepo)

            // Add sample preview data
            runBlocking {
                fakeRepo.add(
                    TransactionEntity(
                        id = "1",
                        title = "Groceries",
                        amount = 45.20,
                        category = "Food & Dining",
                        date = "2024-11-11",
                        type = "EXPENSE"
                    )
                )
                fakeRepo.add(
                    TransactionEntity(
                        id = "2",
                        title = "Salary",
                        amount = 2200.00,
                        category = "Income",
                        date = "2024-11-01",
                        type = "INCOME"
                    )
                )
            }

            return vm
        }
    }



}
