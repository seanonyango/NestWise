package com.example.nestwise.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.nestwise.data.entities.UserEntity
import com.example.nestwise.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel(
    private val repo: UserRepository
) : ViewModel() {

    private val _currentUser = MutableStateFlow<UserEntity?>(null)
    val currentUser: StateFlow<UserEntity?> = _currentUser
    val userRepository: UserRepository = repo


    private val _authError = MutableStateFlow<String?>(null)
    val authError: StateFlow<String?> = _authError

    fun login(email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val user = repo.login(email, password)
            if (user != null) {
                _currentUser.value = user
                _authError.value = null
                onSuccess()
            } else {
                _authError.value = "Invalid email or password"
            }
        }
    }

    fun register(
        name: String,
        email: String,
        password: String,
        currency: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            val result = repo.registerUser(name, email, password, currency)
            result.onSuccess {
                _authError.value = null
                onSuccess()
            }.onFailure {
                _authError.value = it.message ?: "Registration failed"
            }
        }
    }

    fun updateProfile(name: String, currency: String) {
        val user = _currentUser.value ?: return
        val updated = user.copy(name = name, currency = currency)

        viewModelScope.launch {
            repo.updateUser(updated)
            _currentUser.value = updated
        }
    }

    fun logout(onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            userRepository.logout()
            onComplete()
        }
    }

}

class UserViewModelFactory(
    private val repo: UserRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
