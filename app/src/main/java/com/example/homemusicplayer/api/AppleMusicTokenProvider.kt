package com.example.homemusicplayer.api

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.apple.android.sdk.authentication.TokenProvider

/**
 * Copyright (C) 2018 Apple, Inc. All rights reserved.
 */
class AppleMusicTokenProvider(
    private val context: Context
) : TokenProvider {


//    private val JWT_TOKEN_KEY = stringPreferencesKey("jwt_token")
//    private val USER_TOKEN_KEY = stringPreferencesKey("user_token")

    val userToken = MutableLiveData("")
    val devToken = MutableLiveData("")

//    init {
//        CoroutineScope(Dispatchers.IO).launch {
//            context.dataStore.data.map { preferences ->
//                preferences[TokenManager.JWT_TOKEN_KEY]
//            }.collect {
//                devToken.postValue(it)
//            }
//
//            context.dataStore.data.map { preferences ->
//                preferences[TokenManager.USER_TOKEN_KEY]
//            }.collect {
//                userToken.postValue(it)
//            }
//        }
//    }

    override fun getDeveloperToken(): String {
        return "eyJhbGciOiJFUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6IjhQRjgyNlVCVk4ifQ.eyJpc3MiOiI0TjNTVFY3NTVXIiwiaWF0IjoxNjk0NjIxODI4LCJleHAiOjE2OTQ3MDgyMjh9.wVreV7GBZfgmWphx6FRAWOIRnjXSeJ1-OPnCXUti0eog_4m5UocTj9x_dInFAVvfEkSAKDhEU4UoxugDi29-OQ"
    }

    override fun getUserToken(): String {
        return "Agq2Ol42LFVkOy7F/D22dkqV1CF08YtBJdENs0FThIf2snv0D+z/wZ3wI2AxD0vh0HkSXnvvhwONmVI50yD1t1lpYqDiRnPxjxRj9xp/NiXWH6oUNxd1jxQMzKNagbI+1+5/lev2uGm9qYh1xN4rv+c1Mty+V0eG4Nja/RQY1mnicmlToWWisWEyazrVp6LZmO1YyrLt9iqk7BAyQZGnWENzKcfH4GUzUUXtew0rIRpmbCgAsg=="
    }


}
