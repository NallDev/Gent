package com.nalldev.gent.data.datasources.local

import com.nalldev.gent.data.models.EventBookmarkEntity
import com.nalldev.gent.data.models.EventEntity
import com.nalldev.gent.utils.DatabaseException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class LocalDatasource(
    private val eventDao: EventDao,
    private val eventBookmarkDao: EventBookmarkDao,
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun getUpcomingEvent(): List<EventEntity> = withContext(ioDispatcher) {
        try {
            eventDao.getUpcomingEvent()
        } catch (e: Exception) {
            throw DatabaseException("Failed to load upcoming events from database")
        }
    }

    suspend fun getFinishedEvent(): List<EventEntity> = withContext(ioDispatcher) {
        try {
            eventDao.getFinishedEvent()
        } catch (e: Exception) {
            throw DatabaseException("Failed to load finished events from database")
        }
    }

    suspend fun updateEvents(active: Int, events: List<EventEntity>) = withContext(ioDispatcher) {
        try {
            eventDao.updateEventData(active, events)
        } catch (e: Exception) {
            throw DatabaseException("Failed to update events in database")
        }
    }

    suspend fun getEventBookmark(): List<EventBookmarkEntity> = withContext(ioDispatcher) {
        try {
            eventBookmarkDao.getEventBookmark()
        } catch (e: Exception) {
            throw DatabaseException("Failed to load bookmark events from database")
        }
    }

    suspend fun updateEventBookmark(event: EventBookmarkEntity) = withContext(ioDispatcher) {
        try {
            eventBookmarkDao.updateEventBookmark(event)
        } catch (e: Exception) {
            throw DatabaseException("Failed to update bookmark event in database")
        }
    }
}