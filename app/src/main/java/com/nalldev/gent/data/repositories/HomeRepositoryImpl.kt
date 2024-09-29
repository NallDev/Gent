package com.nalldev.gent.data.repositories

import com.nalldev.gent.data.datasources.local.LocalDatasource
import com.nalldev.gent.data.datasources.remote.RemoteDatasource
import com.nalldev.gent.domain.models.EventModel
import com.nalldev.gent.domain.repositories.HomeRepository
import com.nalldev.gent.utils.DataMapper

class HomeRepositoryImpl(
    private val remoteDatasource: RemoteDatasource,
    private val localDatasource: LocalDatasource
) : HomeRepository {

    override suspend fun fetchEvent(active: Int, keyword: String?): List<EventModel> {
        val response = remoteDatasource.fetchEvent(active, keyword)
        val remoteEvents = response.listEvents?.map { DataMapper.eventToEntity(it, active) } ?: emptyList()

        localDatasource.updateEvents(active, remoteEvents)

        return if (active == 1) {
            getUpcomingEvent()
        } else {
            getFinishedEvent()
        }
    }

    override suspend fun getUpcomingEvent(): List<EventModel> {
        val bookmarkEvent = localDatasource.getEventBookmark()
        return localDatasource.getUpcomingEvent().map { DataMapper.eventEntityToDomain(it, bookmarkEvent.any { bookmark -> bookmark.id == it.id }) }
    }

    override suspend fun getFinishedEvent(): List<EventModel> {
        val bookmarkEvent = localDatasource.getEventBookmark()
        return localDatasource.getFinishedEvent().map { DataMapper.eventEntityToDomain(it, bookmarkEvent.any { bookmark -> bookmark.id == it.id }) }
    }

    override suspend fun getEventBookmark(): List<EventModel> = localDatasource.getEventBookmark().map { DataMapper.bookmarkEntityToDomain(it) }

    override suspend fun updateEventBookmark(event: EventModel) {
        val bookmarkEvent = DataMapper.eventModelToBookmarkEntity(event)
        localDatasource.updateEventBookmark(bookmarkEvent)
    }
}