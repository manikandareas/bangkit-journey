package com.manikandareas.devent.data.datasources.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.manikandareas.devent.data.models.EventFavoriteEntity

@Dao
interface EventFavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEventFavorite(events: EventFavoriteEntity)

    @Query("SELECT * FROM tb_event_favorite")
    suspend fun getEventFavorite(): List<EventFavoriteEntity>

    @Query("SELECT * FROM tb_event_favorite WHERE id = :id")
    suspend fun getEventFavoriteById(id : Int): EventFavoriteEntity?

    @Query("DELETE FROM tb_event_favorite WHERE id = :id")
    suspend fun deleteEventFavorite(id : Int)

    @Transaction
    suspend fun updateEventFavorite(events: EventFavoriteEntity) {
        val favoriteEvent = getEventFavoriteById(events.id)
        if (favoriteEvent != null) {
            deleteEventFavorite(events.id)
        } else {
            insertEventFavorite(events)
        }
    }
}