package com.nalldev.gent.data.datasources.local

import com.nalldev.gent.data.models.EventBookmarkEntity
import com.nalldev.gent.data.models.EventEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class LocalDatasource(
    private val eventDao: EventDao,
    private val eventBookmarkDao: EventBookmarkDao,
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun getUpcomingEvent(): List<EventEntity> = withContext(ioDispatcher) {
        eventDao.getUpcomingEvent()
    }

    suspend fun getFinishedEvent(): List<EventEntity> = withContext(ioDispatcher) {
        eventDao.getFinishedEvent()
    }

    suspend fun updateEvents(active: Int, events: List<EventEntity>) = withContext(ioDispatcher) {
        eventDao.updateEventData(active, events)
    }

    suspend fun getEventBookmark(): List<EventBookmarkEntity> = withContext(ioDispatcher) {
        eventBookmarkDao.getEventBookmark()
    }

    suspend fun updateEventBookmark(event: EventBookmarkEntity) = withContext(ioDispatcher) {
        eventBookmarkDao.updateEventBookmark(event)
    }
}