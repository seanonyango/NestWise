package com.example.nestwise.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.nestwise.data.dao.DailyTipDao
import com.example.nestwise.data.entities.DailyTipEntity
import com.example.nestwise.data.remote.AdviceApi
import com.example.nestwise.data.remote.ZenQuotesApi
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class AdviceRepository(
    private val api: ZenQuotesApi,
    private val dao: DailyTipDao
) {

    val latestTip = dao.getLatestTip()

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun refreshTip() {
        // 🔹 Call the "today" endpoint instead of random
        val response = api.getTodayQuote()

        // ZenQuotes returns a 1-element list for /today
        val item = response.firstOrNull() ?: return

        val today = java.time.LocalDate.now().toString()

        val entity = DailyTipEntity(
            advice = item.q,   // quote text
            fetchedAt = today, // date we fetched
            source = item.a    // author
        )

        dao.insertTip(entity)
    }
}


