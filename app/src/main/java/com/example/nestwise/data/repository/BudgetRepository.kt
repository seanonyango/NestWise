package com.example.nestwise.data.repository

import com.example.nestwise.data.dao.BudgetDao
import com.example.nestwise.data.entities.BudgetEntity
import com.example.nestwise.data.entities.BudgetTransactionCrossRef
import com.example.nestwise.data.entities.TransactionEntity
import kotlinx.coroutines.flow.Flow

class BudgetRepository(private val budgetDao: BudgetDao) {

    val budgets: Flow<List<BudgetEntity>> = budgetDao.getAllBudgets()

    suspend fun addBudget(budget: BudgetEntity) {
        budgetDao.insertBudget(budget)
    }

    suspend fun updateBudget(budget: BudgetEntity) {
        budgetDao.updateBudget(budget)
    }

    suspend fun deleteBudget(budget: BudgetEntity) {
        budgetDao.deleteBudget(budget)
    }

    suspend fun deleteById(id: String) {
        budgetDao.deleteById(id)
    }

    suspend fun getBudgetById(id: String): BudgetEntity? {
        return budgetDao.getBudgetById(id)
    }

    suspend fun linkTransaction(budgetId: String, transaction: TransactionEntity) {
        budgetDao.linkTransactionToBudget(
            BudgetTransactionCrossRef(budgetId, transaction.id)
        )
        budgetDao.addToSpent(budgetId, transaction.amount)
    }

    suspend fun unlinkTransaction(budgetId: String, transaction: TransactionEntity) {
        budgetDao.unlinkTransactionFromBudget(budgetId, transaction.id)
        budgetDao.removeFromSpent(budgetId, transaction.amount)
    }

    suspend fun getLinkedBudget(transactionId: String): BudgetTransactionCrossRef? {
        return budgetDao.getBudgetLink(transactionId)
    }

}
