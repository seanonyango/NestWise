package com.example.nestwise.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nestwise.data.entities.GoalEntity
import com.example.nestwise.data.repository.GoalRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GoalViewModel(
    private val repo: GoalRepository
) : ViewModel() {

    val goals = repo.goals
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun addGoal(title: String, target: Double, deadline: String?) {
        val goal = GoalEntity(
            title = title,
            targetAmount = target,
            deadline = deadline
        )
        viewModelScope.launch { repo.add(goal) }
    }

    fun updateGoal(entity: GoalEntity) {
        viewModelScope.launch { repo.update(entity) }
    }

    fun deleteGoal(entity: GoalEntity) {
        viewModelScope.launch { repo.delete(entity) }
    }
}
