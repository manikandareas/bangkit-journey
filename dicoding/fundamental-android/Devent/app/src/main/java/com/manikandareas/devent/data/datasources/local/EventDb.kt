package com.manikandareas.devent.data.datasources.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.manikandareas.devent.data.models.EventFavoriteEntity
import com.manikandareas.devent.data.models.EventEntity

@Database(entities = [EventEntity::class, EventFavoriteEntity::class], version = 2)
abstract class EventDb : RoomDatabase() {
    abstract fun getEventDao(): EventDao
    abstract fun getEventFavoriteDao(): EventFavoriteDao

    companion object {
        private var INSTANCE: EventDb? = null
        private const val DATABASE_NAME = "event_database"

        fun getInstance(context: Context): EventDb {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EventDb::class.java,
                    DATABASE_NAME
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}