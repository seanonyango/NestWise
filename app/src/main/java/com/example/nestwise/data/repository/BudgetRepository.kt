package com.example.nestwise.data.repository


import com.example.nestwise.data.dao.BudgetDao
import com.example.nestwise.data.entities.BudgetEntity
import kotlinx.coroutines.flow.Flow

class BudgetRepository(private val dao: BudgetDao) {

    val budgets: Flow<List<BudgetEntity>> = dao.getAllBudgets()

    suspend fun addBudget(budget: BudgetEntity) {
        dao.insertBudget(budget)
    }

    suspend fun updateBudget(budget: BudgetEntity) {
        dao.updateBudget(budget)
    }

    suspend fun deleteBudget(budget: BudgetEntity) {
        dao.deleteBudget(budget)
    }

    suspend fun deleteById(id: String) {
        dao.deleteById(id)
    }

    suspend fun getBudget(id: String): BudgetEntity? {
        return dao.getBudgetById(id)
    }
}
