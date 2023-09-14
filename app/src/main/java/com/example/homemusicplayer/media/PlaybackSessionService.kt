package com.example.homemusicplayer.media

import android.app.PendingIntent
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import androidx.media.MediaBrowserServiceCompat
import com.apple.android.music.playback.controller.MediaPlayerController
import com.apple.android.music.playback.controller.MediaPlayerControllerFactory
import com.apple.android.music.playback.model.MediaPlayerException
import com.apple.android.music.playback.model.PlayerQueueItem
import com.apple.android.music.sdk.testapp.service.MediaSessionManager
import com.example.homemusicplayer.api.AppleMusicTokenProvider
import com.example.homemusicplayer.data.SearchRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PlaybackSessionService : MediaBrowserServiceCompat() {

    @Inject
    lateinit var searchRepository: SearchRepository

    lateinit var playerController: MediaPlayerController

    private lateinit var mediaSession: MediaSessionCompat

    lateinit var mediaProvider: MediaProvider

    companion object {

        private const val TAG = "MediaPlayerService"

        var currentDuration: Long = 0L
            private set
    }

    init {
        try {
            // Adding these two lines will prevent the OOM false alarm
            System.setProperty("org.bytedeco.javacpp.maxphysicalbytes", "0")
            System.setProperty("org.bytedeco.javacpp.maxbytes", "0")

            System.loadLibrary("c++_shared")
            System.loadLibrary("appleMusicSDK")
        } catch (e: Exception) {
            Log.e(TAG, "Could not load library due to: " + Log.getStackTraceString(e))
            throw e
        }
    }

    override fun onCreate() {
        super.onCreate()

        playerController =
            MediaPlayerControllerFactory.createLocalController(this, AppleMusicTokenProvider(this))
        playerController.addListener(MediaPlayerControllerListener())

        val sessionActivityIntent = packageManager
            ?.getLaunchIntentForPackage(packageName)
            ?.let { sessionIntent ->
                PendingIntent.getActivity(
                    this,
                    0,
                    sessionIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            }

        mediaSession = MediaSessionCompat(this, TAG).apply {
            setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS or
                        MediaSessionCompat.FLAG_HANDLES_QUEUE_COMMANDS
            )
            setCallback(
                MediaSessionManager(
                    playerController,
                    this
                )
            )
        }

        sessionToken = mediaSession.sessionToken
        mediaProvider = MediaProvider(this, searchRepository)
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot {
        return BrowserRoot(K.MEDIA_ROOT_ID, null)
    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        mediaProvider.loadMediaItems(parentId, result)
    }

    override fun onSearch(
        query: String,
        extras: Bundle?,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        mediaProvider.searchQuery(query, result)
    }


    inner class MediaPlayerControllerListener : MediaPlayerController.Listener {

        override fun onPlayerStateRestored(p0: MediaPlayerController) {
        }

        override fun onPlaybackStateChanged(
            p0: MediaPlayerController,
            p1: Int,
            currentState: Int
        ) {
        }

        override fun onPlaybackStateUpdated(p0: MediaPlayerController) {
        }

        override fun onBufferingStateChanged(p0: MediaPlayerController, p1: Boolean) {
        }

        override fun onCurrentItemChanged(
            p0: MediaPlayerController,
            p1: PlayerQueueItem?,
            p2: PlayerQueueItem?
        ) {
        }

        override fun onItemEnded(p0: MediaPlayerController, p1: PlayerQueueItem, p2: Long) {
        }

        override fun onMetadataUpdated(p0: MediaPlayerController, p1: PlayerQueueItem) {
        }

        override fun onPlaybackQueueChanged(
            p0: MediaPlayerController,
            p1: MutableList<PlayerQueueItem>
        ) {
        }

        override fun onPlaybackQueueItemsAdded(
            p0: MediaPlayerController,
            p1: Int,
            p2: Int,
            p3: Int
        ) {
        }

        override fun onPlaybackError(p0: MediaPlayerController, p1: MediaPlayerException) {
            Log.e("PlaybackSessionService", p1.message ?: "no message")
        }

        override fun onPlaybackRepeatModeChanged(p0: MediaPlayerController, p1: Int) {
        }

        override fun onPlaybackShuffleModeChanged(p0: MediaPlayerController, p1: Int) {
        }
    }


}