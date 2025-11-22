package com.example.nestwise.data.repository

import com.example.nestwise.data.dao.GoalDao
import com.example.nestwise.data.entities.GoalEntity
import kotlinx.coroutines.flow.Flow

class GoalRepository(
    private val dao: GoalDao
) {

    val goals: Flow<List<GoalEntity>> = dao.getAllGoals()

    suspend fun add(goal: GoalEntity) = dao.insertGoal(goal)

    suspend fun update(goal: GoalEntity) = dao.updateGoal(goal)

    suspend fun delete(goal: GoalEntity) = dao.deleteGoal(goal)

    suspend fun deleteById(id: String) = dao.deleteById(id)

    suspend fun getGoal(id: String) = dao.getGoalById(id)
}
