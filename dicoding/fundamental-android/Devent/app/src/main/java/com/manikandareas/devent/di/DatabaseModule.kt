package com.manikandareas.devent.di

import android.app.Application
import androidx.room.Room
import com.manikandareas.devent.data.datasources.local.EventFavoriteDao
import com.manikandareas.devent.data.datasources.local.EventDao
import com.manikandareas.devent.data.datasources.local.EventDb
import org.koin.dsl.module

fun provideDatabase(application: Application): EventDb =
    Room.databaseBuilder(
        application,
        EventDb::class.java,
        "event_db"
    )
        .fallbackToDestructiveMigration()
        .build()

fun provideEventDao(eventDb: EventDb): EventDao = eventDb.getEventDao()

fun provideEventFavoriteDao(eventDb: EventDb): EventFavoriteDao = eventDb.getEventFavoriteDao()

val databaseModule= module {
    single { provideDatabase(get()) }
    single { provideEventDao(get()) }
    single { provideEventFavoriteDao(get()) }
}