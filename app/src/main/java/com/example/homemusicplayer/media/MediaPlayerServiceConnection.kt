package com.example.homemusicplayer.media

import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class MediaPlayerServiceConnection @Inject constructor(
    @ApplicationContext context: Context
) {

    private val _playerBackState: MutableStateFlow<PlaybackStateCompat?> = MutableStateFlow(null)
    val playbackState = _playerBackState.asStateFlow()

    private val _isConnected: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isConnected = _isConnected.asStateFlow()

    val currentPlayingAudio = mutableStateOf<MediaStore.Audio?>(null)

    lateinit var mediaControllerCompat: MediaControllerCompat

    private val mediaBrowserServiceCallback = MediaBrowserConnectionCallback(context)

    private val mediaBrowser = MediaBrowserCompat(
        context,
        ComponentName(context, PlaybackSessionService::class.java),
        mediaBrowserServiceCallback,
        null
    ).apply {
        connect()
    }

    val transportControl: MediaControllerCompat.TransportControls
        get() = mediaControllerCompat.transportControls

    val rootMediaId: String
        get() = mediaBrowser.root

    private inner class MediaBrowserConnectionCallback(
        private val context: Context
    ) : MediaBrowserCompat.ConnectionCallback() {

        override fun onConnected() {
            _isConnected.value = true
            Log.e(this@MediaPlayerServiceConnection::class.java.toString(), "onConnected")
            mediaControllerCompat = MediaControllerCompat(
                context,
                mediaBrowser.sessionToken
            ).apply {
            }
        }

        override fun onConnectionSuspended() {
            _isConnected.value = false
        }

        override fun onConnectionFailed() {
            _isConnected.value = false
        }
    }

    fun searchQuery(query: String) {
        mediaBrowser.search(query, null, object : MediaBrowserCompat.SearchCallback() {
            override fun onSearchResult(
                query: String,
                extras: Bundle?,
                items: MutableList<MediaBrowserCompat.MediaItem>
            ) {
                Log.e("MediaPlayerServiceConn", "items are ${items.size}")
            }
        })
    }


    private inner class MediaBrowserCallback : MediaBrowserCompat.ItemCallback() {

        override fun onItemLoaded(item: MediaBrowserCompat.MediaItem?) {
            super.onItemLoaded(item)
        }

    }


    private inner class MediaControllerCallback : MediaControllerCompat.Callback() {

        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
            super.onPlaybackStateChanged(state)
            _playerBackState.value = state
        }

        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            super.onMetadataChanged(metadata)

        }

    }
}