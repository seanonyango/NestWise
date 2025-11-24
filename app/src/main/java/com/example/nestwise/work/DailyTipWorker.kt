package com.example.nestwise.work

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.nestwise.di.AppContainer
import com.example.nestwise.notifications.NotificationHelper

class DailyTipWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    private val container = AppContainer(context)
    private val adviceRepository = container.adviceRepository

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun doWork(): Result {
        return try {
            adviceRepository.refreshTip()
            NotificationHelper.showNewTipNotification(applicationContext)
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }
}

