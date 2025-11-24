package com.example.nestwise.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.core.app.NotificationCompat
import java.time.LocalDate

object NotificationHelper {

    const val CHANNEL_ID = "daily_tip_channel"
    private const val PREFS_NAME = "daily_tip_prefs"
    private const val PREF_KEY_LAST_SENT_DATE = "last_tip_notification_date"

    fun createChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Daily Tips",
                NotificationManager.IMPORTANCE_DEFAULT
            )

            val nm = context.getSystemService(NotificationManager::class.java)
            nm.createNotificationChannel(channel)
        }
    }

    fun showNewTipNotification(context: Context) {


        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        val today = LocalDate.now().toString()
        val lastSent = prefs.getString(PREF_KEY_LAST_SENT_DATE, null)

        // If today's notification already sent → return
        if (lastSent == today) {
            return
        }

        // Otherwise continue and send it
        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Build notification
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("NestWise Daily Tip")
            .setContentText("A new financial tip is ready — tap to view!")
            .setSmallIcon(com.example.nestwise.R.drawable.ic_nestwise_notification)
            .setAutoCancel(true)
            .build()

        nm.notify(2001, notification)

        // Save today's date so it's not sent again
        prefs.edit().putString(PREF_KEY_LAST_SENT_DATE, today).apply()
    }
}
