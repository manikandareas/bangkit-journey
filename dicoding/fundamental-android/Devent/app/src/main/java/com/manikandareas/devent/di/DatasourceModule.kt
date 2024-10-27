package com.manikandareas.devent.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.manikandareas.devent.data.datasources.local.EventFavoriteDao
import com.manikandareas.devent.data.datasources.local.EventDao
import com.manikandareas.devent.data.datasources.local.LocalDatasource
import com.manikandareas.devent.data.datasources.preference.PreferenceDatasource
import com.manikandareas.devent.data.datasources.remote.ApiService
import com.manikandareas.devent.data.datasources.remote.RemoteDatasource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "preferences")

fun provideRemoteDatasource(
    apiService: ApiService,
    context: Context,
    ioDispatcher: CoroutineDispatcher
): RemoteDatasource = RemoteDatasource(apiService, context, ioDispatcher)

fun provideLocalDatasource(
    eventDao: EventDao,
    eventFavoriteDao: EventFavoriteDao,
    ioDispatcher: CoroutineDispatcher
): LocalDatasource = LocalDatasource(eventDao, eventFavoriteDao, ioDispatcher)

fun provideDataStore(context: Context): DataStore<Preferences> = context.dataStore

fun providePreferenceDatastore(
    dataStore: DataStore<Preferences>,
    ioDispatcher: CoroutineDispatcher
): PreferenceDatasource = PreferenceDatasource(dataStore, ioDispatcher)

val datasourceModule = module {
    single(named("IODispatcher")) {
        Dispatchers.IO
    }
    single { provideDataStore(get()) }

    factory { provideRemoteDatasource(get(), get(), get(named("IODispatcher"))) }
    factory { provideLocalDatasource(get(), get(), get(named("IODispatcher"))) }
    factory { providePreferenceDatastore(get(), get(named("IODispatcher"))) }
}