package com.nalldev.gent.data.datasources.local

import com.nalldev.gent.data.models.EventEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class LocalDatasource(
    private val eventDao: EventDao,
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun insertEvent(events: List<EventEntity>) = withContext(
        ioDispatcher
    ) {
        eventDao.insertEvent(events)
    }

    suspend fun getUpcomingEvent(): List<EventEntity> = withContext(ioDispatcher) {
        eventDao.getUpcomingEvent()
    }

    suspend fun getFinishedEvent(): List<EventEntity> = withContext(ioDispatcher) {
        eventDao.getFinishedEvent()
    }

    suspend fun updateEvents(active: Int, events: List<EventEntity>) = withContext(ioDispatcher) {
        eventDao.updateEventData(active, events)
    }
}