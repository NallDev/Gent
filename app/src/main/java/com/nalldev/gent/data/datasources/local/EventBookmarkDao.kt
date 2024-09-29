package com.nalldev.gent.data.datasources.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.nalldev.gent.data.models.EventBookmarkEntity
import com.nalldev.gent.data.models.EventEntity

@Dao
interface EventBookmarkDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEventBookmark(events: EventBookmarkEntity)

    @Query("SELECT * FROM tb_event_bookmark")
    suspend fun getEventBookmark(): List<EventBookmarkEntity>

    @Query("SELECT * FROM tb_event_bookmark WHERE id = :id")
    suspend fun getEventBookmarkById(id : Int): EventBookmarkEntity?

    @Query("DELETE FROM tb_event_bookmark WHERE id = :id")
    suspend fun deleteEventBookmark(id : Int)

    @Transaction
    suspend fun updateEventBookmark(events: EventBookmarkEntity) {
        val bookmarkEvent = getEventBookmarkById(events.id)
        if (bookmarkEvent != null) {
            deleteEventBookmark(events.id)
        } else {
            insertEventBookmark(events)
        }
    }
}