package com.manikandareas.devent.data.datasources.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class PreferenceDatasource(
    private val dataStore: DataStore<Preferences>,
    private val ioDispatcher: CoroutineDispatcher
) {

    val isDarkMode: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[IS_DARK_MODE_KEY] ?: false
        }

    suspend fun setIsDarkMode(isDarkMode: Boolean) = withContext(ioDispatcher) {
        dataStore.edit { preferences ->
            preferences[IS_DARK_MODE_KEY] = isDarkMode
        }
    }

    val isDailyReminderEnabled: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[IS_DAILY_REMINDER_ENABLED] == true
        }

    suspend fun setIsDailyReminderEnabled(enabled: Boolean) = withContext(ioDispatcher) {
        dataStore.edit { preferences ->
            preferences[IS_DAILY_REMINDER_ENABLED] = enabled
        }
    }

    val isFirstTime: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[IS_FIRST_TIME_KEY] != false
    }

    suspend fun setIsFirstTime(isFirstTime: Boolean) = withContext(ioDispatcher) {
        dataStore.edit { preferences ->
            preferences[IS_FIRST_TIME_KEY] = isFirstTime
        }
    }

    companion object {
        private val IS_DARK_MODE_KEY = booleanPreferencesKey("isDarkMode")
        private val IS_DAILY_REMINDER_ENABLED = booleanPreferencesKey("isDailyReminderEnabled")
        private val IS_FIRST_TIME_KEY = booleanPreferencesKey("isFirstTime")
    }
}