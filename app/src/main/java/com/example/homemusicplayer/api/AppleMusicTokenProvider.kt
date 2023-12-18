package com.example.homemusicplayer.api

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.apple.android.sdk.authentication.TokenProvider
import com.example.homemusicplayer.BuildConfig
import com.example.homemusicplayer.di.dataStore
import com.example.homemusicplayer.persist.TokenManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


/**
 * This is the implementation of the TokenProvider interface that hooks into the
 * MediaControllerFactory to support music playback, as defined by Apple's documentation
 *
 * Currently, we need a way to hook into the saved tokens that are stored in the TokenManage;
 * this is yet to be done
 */
class AppleMusicTokenProvider(
    private val context: Context
) : TokenProvider {


    // This does nothing right now are we are still unable to get the tokens in a static form
    val userToken = MutableLiveData("")
    val devToken = MutableLiveData("")

    // This does nothing right now are we are still unable to get the tokens in a static form
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
        return BuildConfig.developerToken
    }

    override fun getUserToken(): String {
        return BuildConfig.userToken
    }


}
