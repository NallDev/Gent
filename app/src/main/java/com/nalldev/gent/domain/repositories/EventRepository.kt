package com.nalldev.gent.domain.repositories

import com.nalldev.gent.domain.models.EventModel
import kotlinx.coroutines.flow.Flow


interface EventRepository {
    suspend fun fetchEvent(active: Int, keyword: String? = null): List<EventModel>
    suspend fun getUpcomingEvent(): List<EventModel>
    suspend fun getFinishedEvent(): List<EventModel>
    suspend fun getEventBookmark(): List<EventModel>
    suspend fun updateEventBookmark(event: EventModel)
    fun getIsDarkMode(): Flow<Boolean>
    suspend fun setIsDarkMode(isDarkMode: Boolean)
    fun getIsNotificationEnabled(): Flow<Boolean>
    suspend fun setIsNotificationEnabled(enabled: Boolean)
}