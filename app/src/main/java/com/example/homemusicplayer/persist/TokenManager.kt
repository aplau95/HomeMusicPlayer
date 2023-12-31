package com.example.homemusicplayer.persist

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.homemusicplayer.di.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Helper class that saves generated tokens into our datastore
 */
class TokenManager(private val context: Context) {

    companion object {

        val JWT_TOKEN_KEY = stringPreferencesKey("jwt_token")
        val USER_TOKEN_KEY = stringPreferencesKey("user_token")
    }

    fun getJWTToken(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[JWT_TOKEN_KEY]
        }
    }

    suspend fun saveJWTToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[JWT_TOKEN_KEY] = token
        }
    }

    suspend fun deleteJWTToken() {
        context.dataStore.edit { preferences ->
            preferences.remove(JWT_TOKEN_KEY)
        }
    }

    fun getUserToken(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[USER_TOKEN_KEY]
        }
    }

    suspend fun saveUserToken(token: String) {
        Log.e("TokenManager", "saving user token of $token")
        context.dataStore.edit { preferences ->
            preferences[USER_TOKEN_KEY] = token
        }
    }

    suspend fun deleteUserToken() {
        context.dataStore.edit { preferences ->
            preferences.remove(USER_TOKEN_KEY)
        }
    }
}