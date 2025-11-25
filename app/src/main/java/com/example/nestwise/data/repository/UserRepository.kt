package com.example.nestwise.data.repository

import android.content.Context
import com.example.nestwise.data.dao.UserDao
import com.example.nestwise.data.entities.UserEntity

class UserRepository(
    private val userDao: UserDao,
    private val context: Context
) {

    // Shared Preferences for login session
    private val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    // ------------------------------------------------------------
    // USER REGISTRATION (Room)
    // ------------------------------------------------------------
    suspend fun registerUser(
        name: String,
        email: String,
        password: String,
        currency: String
    ): Result<Unit> {

        val existing = userDao.getUserByEmail(email)
        if (existing != null) {
            return Result.failure(IllegalArgumentException("Email already registered"))
        }

        val user = UserEntity(
            name = name,
            email = email,
            password = password,
            currency = currency
        )
        userDao.insertUser(user)

        return Result.success(Unit)
    }

    // ------------------------------------------------------------
    // LOGIN (Room + SharedPreferences session)
    // ------------------------------------------------------------
    suspend fun login(email: String, password: String): UserEntity? {
        val user = userDao.getUserByEmail(email) ?: return null

        return if (user.password == password) {
            saveSession(user)   // store session
            user
        } else null
    }

    // ------------------------------------------------------------
    // UPDATE USER PROFILE (Room)
    // ------------------------------------------------------------
    suspend fun updateUser(user: UserEntity) {
        userDao.updateUser(user)
        saveSession(user) // update session too (currency changes)
    }

    // ------------------------------------------------------------
    // SESSION MANAGEMENT
    // ------------------------------------------------------------

    private fun saveSession(user: UserEntity) {
        prefs.edit()
            .putString("email", user.email)
            .putString("currency", user.currency)
            .apply()
    }

    fun getLoggedInUser(): UserEntity? {
        val email = prefs.getString("email", null) ?: return null
        return userDao.getUserByEmailNonSuspend(email) // helper DAO for sync calls
    }

    fun getSavedCurrency(): String {
        return prefs.getString("currency", "AUD") ?: "AUD"
    }

    fun logout() {
        prefs.edit().clear().apply()
    }
}
