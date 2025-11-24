package com.example.nestwise.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.nestwise.data.entities.DailyTipEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyTipDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTip(tip: DailyTipEntity)

    @Query("SELECT * FROM daily_tips ORDER BY fetchedAt DESC LIMIT 1")
    fun getLatestTip(): Flow<DailyTipEntity?>

    @Query("DELETE FROM daily_tips")
    suspend fun clearAll()
}
