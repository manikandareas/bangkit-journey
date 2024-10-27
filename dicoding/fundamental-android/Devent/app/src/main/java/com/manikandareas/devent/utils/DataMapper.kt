package com.manikandareas.devent.utils

import com.manikandareas.devent.data.models.EventFavoriteEntity
import com.manikandareas.devent.data.models.EventEntity
import com.manikandareas.devent.domain.models.EventItem
import com.manikandareas.devent.domain.models.EventModel
object DataMapper {
    fun eventToEntity(
        event: EventItem,
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

    fun eventEntityToDomain(event: EventEntity, isFavorite : Boolean): EventModel = EventModel(
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
        isFavorite = isFavorite
    )

    fun favoriteEntityToDomain(event: EventFavoriteEntity): EventModel = EventModel(
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
        isFavorite = true
    )

    fun eventModelToFavoriteEntity(event: EventModel): EventFavoriteEntity = EventFavoriteEntity(
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