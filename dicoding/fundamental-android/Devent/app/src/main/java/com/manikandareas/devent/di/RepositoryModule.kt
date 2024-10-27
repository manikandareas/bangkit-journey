package com.manikandareas.devent.di

import android.content.Context
import com.manikandareas.devent.data.datasources.local.LocalDatasource
import com.manikandareas.devent.data.datasources.preference.PreferenceDatasource
import com.manikandareas.devent.data.datasources.remote.RemoteDatasource
import com.manikandareas.devent.data.repositories.EventRepositoryImpl
import com.manikandareas.devent.domain.repositories.EventRepository
import org.koin.dsl.module

fun provideEventRepository(remoteDatasource: RemoteDatasource, localDatasource: LocalDatasource, preferenceDatasource: PreferenceDatasource, context: Context): EventRepository = EventRepositoryImpl(remoteDatasource, localDatasource, preferenceDatasource, context)

val repositoryModule = module {
    factory { provideEventRepository(get(), get(), get(), get()) }
}