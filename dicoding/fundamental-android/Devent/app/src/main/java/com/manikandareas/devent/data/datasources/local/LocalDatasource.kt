package com.manikandareas.devent.data.datasources.local

import com.manikandareas.devent.data.models.EventFavoriteEntity
import com.manikandareas.devent.data.models.EventEntity
import com.manikandareas.devent.utils.DatabaseException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class LocalDatasource(
    private val eventDao: EventDao,
    private val eventFavoriteDao: EventFavoriteDao,
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

    suspend fun getEventFavorite(): List<EventFavoriteEntity> = withContext(ioDispatcher) {
        try {
            eventFavoriteDao.getEventFavorite()
        } catch (e: Exception) {
            throw DatabaseException("Failed to load favorite events from database $e")
        }
    }

    suspend fun updateEventFavorite(event: EventFavoriteEntity) = withContext(ioDispatcher) {
        try {
            eventFavoriteDao.updateEventFavorite(event)
        } catch (e: Exception) {
            throw DatabaseException("Failed to update favorite event in database")
        }
    }
}