package com.example.place3mapbar


import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "user_prefs")

object DataStoreHelper {
    private val USER_NAME_KEY = stringPreferencesKey("user_name")

    suspend fun saveUserName(context: Context, name: String) {
        context.dataStore.edit { prefs ->
            prefs[USER_NAME_KEY] = name
        }
    }

    fun getUserName(context: Context): Flow<String?> {
        return context.dataStore.data.map { prefs ->
            prefs[USER_NAME_KEY]
        }
    }
}
