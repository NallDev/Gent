package com.nalldev.gent.domain.repositories

import com.nalldev.gent.domain.models.EventModel


interface HomeRepository {
    suspend fun fetchEvent(active: Int, keyword: String? = null): List<EventModel>
    suspend fun getUpcomingEvent(): List<EventModel>
    suspend fun getFinishedEvent(): List<EventModel>
}