package com.example.nestwise.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nestwise.data.entities.BudgetEntity
import com.example.nestwise.data.repository.BudgetRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BudgetViewModel(
    private val repo: BudgetRepository
) : ViewModel() {

    // Expose budgets as StateFlow for Compose
    val budgets = repo.budgets
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun addBudget(category: String, limit: Double, notes: String?) {
        val newBudget = BudgetEntity(
            category = category,
            limitAmount = limit,
            notes = notes
        )

        viewModelScope.launch {
            repo.addBudget(newBudget)
        }
    }

    fun updateBudget(entity: BudgetEntity) {
        viewModelScope.launch {
            repo.updateBudget(entity)
        }
    }

    fun deleteBudget(entity: BudgetEntity) {
        viewModelScope.launch {
            repo.deleteBudget(entity)
        }
    }

    fun deleteById(id: String) {
        viewModelScope.launch {
            repo.deleteById(id)
        }
    }
}
