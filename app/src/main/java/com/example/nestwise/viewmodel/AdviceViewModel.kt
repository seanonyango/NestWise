package com.example.nestwise.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nestwise.data.repository.AdviceRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AdviceViewModel(
    private val repo: AdviceRepository
) : ViewModel() {

    val latestTip = repo.latestTip
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    @RequiresApi(Build.VERSION_CODES.O)
    fun refreshTip() {
        viewModelScope.launch {
            try {
                repo.refreshTip()
            } catch (e: Exception) {
                // For now, ignore / log; can hook Snackbar later
                e.printStackTrace()
            }
        }
    }
}
