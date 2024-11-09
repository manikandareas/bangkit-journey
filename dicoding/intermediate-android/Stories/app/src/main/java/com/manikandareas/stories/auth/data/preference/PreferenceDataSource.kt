package com.manikandareas.stories.auth.data.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class PreferenceDataSource(
    private val dataStore: DataStore<Preferences>,
    private val ioDispatcher: CoroutineDispatcher
) {

    val isLoggedIn: Flow<Boolean> = dataStore.data.map { preferences ->
        val sessionToken = preferences[SESSION_TOKEN] ?: ""
        sessionToken.isNotEmpty()
    }

    suspend fun setSessionToken(token:String) = withContext(ioDispatcher) {
        dataStore.edit { preferences -> preferences[SESSION_TOKEN] = token }
    }

    companion object {
        private val SESSION_TOKEN = stringPreferencesKey("session_token")
    }

    val sessionToken: Flow<String> = dataStore.data.map { preferences ->
        preferences[SESSION_TOKEN] ?: ""
    }
}