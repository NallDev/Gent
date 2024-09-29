package com.nalldev.gent.utils

import com.nalldev.gent.data.models.EventBookmarkEntity
import com.nalldev.gent.data.models.EventEntity
import com.nalldev.gent.domain.models.EventModel
import com.nalldev.gent.domain.models.ListEventsItem

object DataMapper {
    fun eventToEntity(
        event: ListEventsItem,
        active: Int
    ): EventEntity {
        return EventEntity(
            id = event.id ?: 0,
            name = event.name ?: "",
            summary = event.summary ?: "",
            description = event.description ?: "",
            image = event.mediaCover ?: "",
            ownerName = event.ownerName ?: "",
            cityName = event.cityName ?: "",
            beginTime = event.beginTime ?: "",
            endTime = event.endTime ?: "",
            category = event.category ?: "",
            quota = event.quota ?: 0,
            registrants = event.registrants ?: 0,
            link = event.link ?: "",
            active = active
        )
    }

    fun eventEntityToDomain(event: EventEntity, isBookmark : Boolean): EventModel = EventModel(
        id = event.id,
        name = event.name,
        summary = event.summary,
        description = event.description,
        image = event.image,
        ownerName = event.ownerName,
        cityName = event.cityName,
        beginTime = event.beginTime,
        endTime = event.endTime,
        category = event.category,
        quota = event.quota,
        registrants = event.registrants,
        link = event.link,
        isBookmark = isBookmark
    )

    fun bookmarkEntityToDomain(event: EventBookmarkEntity): EventModel = EventModel(
        id = event.id,
        name = event.name,
        summary = event.summary,
        description = event.description,
        image = event.image,
        ownerName = event.ownerName,
        cityName = event.cityName,
        beginTime = event.beginTime,
        endTime = event.endTime,
        category = event.category,
        quota = event.quota,
        registrants = event.registrants,
        link = event.link,
        isBookmark = true
    )

    fun eventModelToBookmarkEntity(event: EventModel): EventBookmarkEntity = EventBookmarkEntity(
        id = event.id,
        name = event.name,
        summary = event.summary,
        description = event.description,
        image = event.image,
        ownerName = event.ownerName,
        cityName = event.cityName,
        beginTime = event.beginTime,
        endTime = event.endTime,
        category = event.category,
        quota = event.quota,
        registrants = event.registrants,
        link = event.link
    )
}