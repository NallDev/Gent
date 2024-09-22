package com.nalldev.gent.data.datasources.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nalldev.gent.data.models.EventEntity

@Database(entities = [(EventEntity::class)], version = 1)
abstract class EventDb : RoomDatabase() {
    abstract fun getEventDao() : EventDao
}