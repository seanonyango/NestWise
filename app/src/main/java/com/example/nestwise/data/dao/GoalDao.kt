package com.example.nestwise.data.dao

import androidx.room.*
import com.example.nestwise.data.entities.GoalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GoalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(goal: GoalEntity)

    @Update
    suspend fun updateGoal(goal: GoalEntity)

    @Delete
    suspend fun deleteGoal(goal: GoalEntity)

    @Query("DELETE FROM goals WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("SELECT * FROM goals ORDER BY deadline ASC")
    fun getAllGoals(): Flow<List<GoalEntity>>

    @Query("SELECT * FROM goals WHERE id = :id LIMIT 1")
    suspend fun getGoalById(id: String): GoalEntity?
}
