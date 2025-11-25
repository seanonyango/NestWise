package com.example.nestwise.di

import android.content.Context
import com.example.nestwise.data.database.AppDatabase
import com.example.nestwise.data.remote.ZenQuotesApi
import com.example.nestwise.data.repository.AdviceRepository
import com.example.nestwise.data.repository.BudgetRepository
import com.example.nestwise.data.repository.GoalRepository
import com.example.nestwise.data.repository.TransactionRepository
import com.example.nestwise.data.repository.UserRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class AppContainer(context: Context) {

    private val database: AppDatabase = AppDatabase.getInstance(context)

    // --- Retrofit setup ---
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://zenquotes.io/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    private val zenQuotesApi: ZenQuotesApi =
        retrofit.create(ZenQuotesApi::class.java)


    val transactionRepository: TransactionRepository =
        TransactionRepository(database.transactionDao())

    val budgetRepository: BudgetRepository =
        BudgetRepository(database.budgetDao())

    val goalRepository: GoalRepository =
        GoalRepository(database.goalDao())

    //  Advice / Tip repository
    val adviceRepository: AdviceRepository =
        AdviceRepository(zenQuotesApi, database.dailyTipDao())

    val userRepository = UserRepository(
        database.userDao(),
        context
    )

}
