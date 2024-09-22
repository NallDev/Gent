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

    override suspend fun getUpcomingEvent(): List<EventModel> = localDatasource.getUpcomingEvent().map { DataMapper.entityToDomain(it) }

    override suspend fun getFinishedEvent(): List<EventModel> = localDatasource.getFinishedEvent().map { DataMapper.entityToDomain(it) }
}