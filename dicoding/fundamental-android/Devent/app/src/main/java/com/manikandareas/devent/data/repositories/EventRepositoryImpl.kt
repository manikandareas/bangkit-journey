package com.manikandareas.devent.data.repositories

import android.content.Context
import com.manikandareas.devent.R
import com.manikandareas.devent.data.datasources.local.LocalDatasource
import com.manikandareas.devent.data.datasources.preference.PreferenceDatasource
import com.manikandareas.devent.data.datasources.remote.RemoteDatasource
import com.manikandareas.devent.domain.models.EventModel
import com.manikandareas.devent.domain.models.StatusEvent
import com.manikandareas.devent.domain.repositories.EventRepository
import com.manikandareas.devent.utils.AppException
import com.manikandareas.devent.utils.DataMapper
import com.manikandareas.devent.utils.UnknownException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException

class EventRepositoryImpl(
    private val remoteDatasource: RemoteDatasource,
    private val localDatasource: LocalDatasource,
    private val preferenceDatasource: PreferenceDatasource,
    private val context: Context
) : EventRepository {

    override suspend fun fetchEvent(active: Int, keyword: String?): List<EventModel> {
        return withContext(Dispatchers.IO) {
            try {
                val response = remoteDatasource.fetchEvent(active, keyword)
                val remoteEvents =
                    response.listEvents?.map { DataMapper.eventToEntity(it, active) } ?: emptyList()
                localDatasource.updateEvents(active, remoteEvents)

                if (!isActive) {
                    throw CancellationException("Search cancelled")
                }

                if (active == StatusEvent.ACTIVE) {
                    getUpcomingEvent()
                } else if (active == StatusEvent.FINISHED) {
                    getFinishedEvent()
                } else {
                    val favoriteEvent = localDatasource.getEventFavorite()
                    remoteEvents.map {
                        DataMapper.eventEntityToDomain(it, favoriteEvent.any { favorite ->
                            favorite.id == it.id
                        })
                    }
                }
            } catch (e: AppException) {
                throw e
            } catch (e: Exception) {
                throw UnknownException(e.message ?: context.getString(R.string.error_code_default))
            }
        }
    }

    override suspend fun getUpcomingEvent(): List<EventModel> {
        val favoriteEvent = localDatasource.getEventFavorite()
        return localDatasource.getUpcomingEvent().map {
            DataMapper.eventEntityToDomain(
                it,
                favoriteEvent.any { favorite -> favorite.id == it.id })
        }
    }


    override suspend fun getFinishedEvent(): List<EventModel> {
        val favoriteEvent = localDatasource.getEventFavorite()
        return localDatasource.getFinishedEvent().map {
            DataMapper.eventEntityToDomain(
                it,
                favoriteEvent.any { favorite -> favorite.id == it.id })
        }
    }

    override suspend fun getEventFavorite(): List<EventModel> =
        localDatasource.getEventFavorite().map { DataMapper.favoriteEntityToDomain(it) }

    override suspend fun updateEventFavorite(event: EventModel) {
        val favoriteEvent = DataMapper.eventModelToFavoriteEntity(event)
        localDatasource.updateEventFavorite(favoriteEvent)
    }

    override fun getIsDarkMode(): Flow<Boolean> {
        return preferenceDatasource.isDarkMode
    }

    override suspend fun setIsDarkMode(isDarkMode: Boolean) {
        preferenceDatasource.setIsDarkMode(isDarkMode)
    }


    override fun getIsDailyReminderEnabled(): Flow<Boolean> {
        return preferenceDatasource.isDailyReminderEnabled
    }

    override suspend fun setIsDailyReminderEnabled(enabled: Boolean) {
        preferenceDatasource.setIsDailyReminderEnabled(enabled)
    }

    override fun getIsFirstTime(): Flow<Boolean> {
        return preferenceDatasource.isFirstTime
    }

    override suspend fun setIsFirstTime(isFirstTime: Boolean) {
        preferenceDatasource.setIsFirstTime(isFirstTime)
    }
}