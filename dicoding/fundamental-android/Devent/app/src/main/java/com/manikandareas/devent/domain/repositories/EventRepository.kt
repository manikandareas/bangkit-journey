package com.manikandareas.devent.domain.repositories

import com.manikandareas.devent.domain.models.EventModel
import kotlinx.coroutines.flow.Flow

interface EventRepository {
    suspend fun fetchEvent(active: Int, keyword: String? = null): List<EventModel>
    suspend fun getUpcomingEvent(): List<EventModel>
    suspend fun getFinishedEvent(): List<EventModel>
    suspend fun getEventFavorite(): List<EventModel>
    suspend fun updateEventFavorite(event: EventModel)
    fun getIsDarkMode(): Flow<Boolean>
    suspend fun setIsDarkMode(isDarkMode: Boolean)
    fun getIsDailyReminderEnabled(): Flow<Boolean>
    suspend fun setIsDailyReminderEnabled(enabled: Boolean)
    fun getIsFirstTime(): Flow<Boolean>
    suspend fun setIsFirstTime(isFirstTime: Boolean)
}