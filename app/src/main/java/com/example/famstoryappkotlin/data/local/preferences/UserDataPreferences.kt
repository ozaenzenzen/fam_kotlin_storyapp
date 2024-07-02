package com.example.famstoryappkotlin.data.local.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserDataPreferences private constructor(private val dataStore: DataStore<Preferences>) {
    private val TOKEN_KEY = stringPreferencesKey("data_token")

    fun getAuthenticationToken(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[TOKEN_KEY]
        }
    }

    suspend fun saveAuthenticationToken(token: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }
}