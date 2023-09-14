package com.example.homemusicplayer.api

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.apple.android.sdk.authentication.TokenProvider
import com.example.homemusicplayer.di.dataStore
import com.example.homemusicplayer.persist.TokenManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

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

    init {
        CoroutineScope(Dispatchers.IO).launch {
            context.dataStore.data.map { preferences ->
                preferences[TokenManager.JWT_TOKEN_KEY]
            }.collect {
                devToken.postValue(it)
            }

            context.dataStore.data.map { preferences ->
                preferences[TokenManager.USER_TOKEN_KEY]
            }.collect {
                userToken.postValue(it)
            }
        }
    }

    override fun getDeveloperToken(): String {
        return "eyJhbGciOiJFUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6IjhQRjgyNlVCVk4ifQ.eyJpc3MiOiI0TjNTVFY3NTVXIiwiaWF0IjoxNjk0NjQ4MzQ3LCJleHAiOjE2OTQ3MzQ3NDd9.2Sv-OPE9l68L_lp-zeSOh8Q5oJDg2n9kf4rx76Qmlww7e8kLeqHMpPqBgLgArfqw-79iCjpx0ocUG7OEv0Bvfw"
//        Log.e("AppleMusicTokenProvider", "developer token is ${devToken.value}")
//        return devToken.value!!
    }

    override fun getUserToken(): String {
//        Log.e("AppleMusicTokenProvider", "user token is ${userToken.value}")
        return "Apb6oRz8Xf3ax7x6Q4AXzR/pC9+apMF854vRv0bozsx1rkMnSWssFQxF3+zhTXrJVLbZpFmhAkq1YjvtzQb+4qO+kgkUwhGE4Ldiy4tOdTlsF62+3Vt9RtRGmNnaEB6oGdl8qspZ0DLjCNHPnbFuOcY+mxt/UUn2qM/ibPt8d33qQf6hhRGGLxyhvkosBPczHeY3CXpvPKcnysDocwN0d5aVDaBoVcvvMCkLkH2/O2mx4LJhHA=="
//        return userToken.value!!
    }


}
