package com.manikandareas.stories.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.manikandareas.stories.auth.data.networking.RemoteAuthDataSource
import com.manikandareas.stories.auth.data.preference.PreferenceDataSource
import com.manikandareas.stories.auth.domain.AuthDataSource
import com.manikandareas.stories.story.data.networking.RemoteStoryDataSource
import com.manikandareas.stories.story.domain.StoryDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "preferences")

fun provideDataStore(context: Context): DataStore<Preferences> = context.dataStore


fun providePreferenceDataSource(
    dataStore: DataStore<Preferences>,
    ioDispatcher: CoroutineDispatcher
): PreferenceDataSource = PreferenceDataSource(dataStore, ioDispatcher)

val dataSourceModule = module {
    single(named("IODispatcher")) {
        Dispatchers.IO
    }
    single { provideDataStore(get()) }
    singleOf(::RemoteAuthDataSource).bind<AuthDataSource>()
    singleOf(::RemoteStoryDataSource).bind<StoryDataSource>()
    factory { providePreferenceDataSource(get(), get(named("IODispatcher"))) }
}