package com.nalldev.gent.data.datasources.preference

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

    val isNotificationEnabled: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[IS_NOTIFICATION_ENABLED_KEY] ?: false
        }

    suspend fun setIsNotificationEnabled(enabled: Boolean) = withContext(ioDispatcher) {
        dataStore.edit { preferences ->
            preferences[IS_NOTIFICATION_ENABLED_KEY] = enabled
        }
    }

    companion object {
        private val IS_DARK_MODE_KEY = booleanPreferencesKey("isDarkMode")
        private val IS_NOTIFICATION_ENABLED_KEY = booleanPreferencesKey("isNotificationEnabled")
    }
}