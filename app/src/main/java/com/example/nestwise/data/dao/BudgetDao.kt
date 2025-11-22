package com.example.nestwise.data.dao


import androidx.room.*
import com.example.nestwise.data.entities.BudgetEntity
import com.example.nestwise.data.entities.BudgetTransactionCrossRef
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBudget(budget: BudgetEntity)

    @Update
    suspend fun updateBudget(budget: BudgetEntity)

    @Delete
    suspend fun deleteBudget(budget: BudgetEntity)

    @Query("DELETE FROM budgets WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("SELECT * FROM budgets ORDER BY category ASC")
    fun getAllBudgets(): Flow<List<BudgetEntity>>

    @Query("SELECT * FROM budgets WHERE id = :id LIMIT 1")
    suspend fun getBudgetById(id: String): BudgetEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun linkTransactionToBudget(ref: BudgetTransactionCrossRef)

    @Query("DELETE FROM BudgetTransactionCrossRef WHERE budgetId = :budgetId AND transactionId = :transactionId")
    suspend fun unlinkTransactionFromBudget(budgetId: String, transactionId: String)

    @Query("SELECT * FROM BudgetTransactionCrossRef WHERE transactionId = :transactionId")
    suspend fun getBudgetLink(transactionId: String): BudgetTransactionCrossRef?

    @Query("UPDATE budgets SET spentAmount = spentAmount + :amount WHERE id = :budgetId")
    suspend fun addToSpent(budgetId: String, amount: Double)

    @Query("UPDATE budgets SET spentAmount = spentAmount - :amount WHERE id = :budgetId")
    suspend fun removeFromSpent(budgetId: String, amount: Double)


}
