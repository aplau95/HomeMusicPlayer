package com.example.homemusicplayer.media

import android.content.ComponentName
import android.content.Context
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/**
 * Responsible for initializing media playback. It houses the MediaControllerCompat which intakes
 * the PlaybackSessionService. This creates the bridge between the native Android MediaBrowserCompat
 * and Apple's special MediaPlayerController.
 */
class MediaPlayerServiceConnection @Inject constructor(
    @ApplicationContext context: Context
) {

    private val _playerBackState: MutableStateFlow<PlaybackStateCompat?> = MutableStateFlow(null)
    val playbackState = _playerBackState.asStateFlow()

    private val _isConnected: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isConnected = _isConnected.asStateFlow()


    lateinit var mediaControllerCompat: MediaControllerCompat

    private val mediaBrowserServiceCallback = MediaBrowserConnectionCallback(context)

    private val mediaControllerCallback = MediaControllerCallback()

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
                this.registerCallback(mediaControllerCallback)
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
        if (query.isEmpty()) return
    }

    // Notification from MediaControllerCompat of any state changes
    private inner class MediaControllerCallback : MediaControllerCompat.Callback() {

        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
            super.onPlaybackStateChanged(state)
            _playerBackState.value = state
        }

        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            Log.e("MediaControllerCallback", "onMetadataChanged")
            super.onMetadataChanged(metadata)

        }

        override fun onQueueTitleChanged(title: CharSequence?) {
            Log.e("MediaControllerCallback", "onQueueTitleChanged ${title}")
            super.onQueueTitleChanged(title)
        }

    }
}