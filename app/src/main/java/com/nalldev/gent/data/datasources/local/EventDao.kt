package com.nalldev.gent.data.datasources.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.nalldev.gent.data.models.EventEntity

@Dao
interface EventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(events: List<EventEntity>)

    @Query("SELECT * FROM tb_event WHERE active = 1")
    suspend fun getUpcomingEvent(): List<EventEntity>

    @Query("SELECT * FROM tb_event WHERE active = 0")
    suspend fun getFinishedEvent(): List<EventEntity>

    @Query("DELETE FROM tb_event WHERE active = 1")
    suspend fun deleteUpcomingEvent()

    @Query("DELETE FROM tb_event WHERE active = 0")
    suspend fun deleteFinishedEvent()

    @Transaction
    suspend fun updateEventData(active: Int, newEvent: List<EventEntity>) {
        if (active == 1) {
            deleteUpcomingEvent()
        } else {
            deleteFinishedEvent()
        }
        insertEvent(newEvent)
    }
}