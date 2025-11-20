package com.example.nestwise.ui.factories


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nestwise.data.repository.BudgetRepository
import com.example.nestwise.viewmodel.BudgetViewModel

class BudgetViewModelFactory(
    private val repo: BudgetRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BudgetViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BudgetViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
