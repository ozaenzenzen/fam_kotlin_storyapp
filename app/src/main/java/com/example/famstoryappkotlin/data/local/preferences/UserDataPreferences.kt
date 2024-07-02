package com.example.famstoryappkotlin.data.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "userdata")
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

    companion object {
        @Volatile
        private var INSTANCE: UserDataPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): UserDataPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = UserDataPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}