package com.example.homemusicplayer.media

import android.content.Intent
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.media.MediaBrowserServiceCompat
import androidx.media.session.MediaButtonReceiver
import com.apple.android.music.playback.controller.MediaPlayerController
import com.apple.android.music.playback.controller.MediaPlayerControllerFactory
import com.apple.android.music.playback.model.MediaPlayerException
import com.apple.android.music.playback.model.PlayerQueueItem
import com.example.homemusicplayer.api.AppleMusicTokenProvider
import com.example.homemusicplayer.data.SearchRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

/**
 * This takes the MediaBrowser and
 */
@AndroidEntryPoint
class PlaybackSessionService : MediaBrowserServiceCompat() {

    @Inject
    lateinit var searchRepository: SearchRepository

    // Part of Apple Music SDK
    lateinit var playerController: MediaPlayerController

    private lateinit var mediaSession: MediaSessionCompat

    lateinit var mediaProvider: MediaProvider

    private lateinit var mediaNotificationManager: MediaNotificationManager

    val backgroundThread = CoroutineScope(Dispatchers.IO)
    val mainThread = CoroutineScope(Dispatchers.Main)

    companion object {

        private val CANONICAL_NAME = PlaybackSessionService::class.java.canonicalName
        val ACTION_CURRENT_ITEM_CHANGED = "$CANONICAL_NAME.action_current_item_changed"
        private const val TAG = "MediaPlayerService"

        var currentDuration: Long = 0L
            private set
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        MediaButtonReceiver.handleIntent(mediaSession, intent)
        return super.onStartCommand(intent, flags, startId)
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

    private fun initMediaNotificationManager() {
        mediaNotificationManager = MediaNotificationManager(this)
    }

    override fun onCreate() {
        super.onCreate()

        initMediaNotificationManager()

        // Create the controller Apple Music playback. We need to use this since we need to provide the
        // AppleMusicTokenProvider to verify that this user has the rights to play music
        playerController =
            MediaPlayerControllerFactory.createLocalController(this, AppleMusicTokenProvider(this))
        playerController.addListener(MediaPlayerControllerListener())

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

    // Update the notification manager to let us know a new item is being played
    private fun updateNotificationForItemChanged(currentItem: PlayerQueueItem?) {

        currentItem?.let {
            val notification = mediaNotificationManager.getNotification(
                it.item,
                mediaSession.sessionToken,
                PlaybackStateCompat.STATE_PLAYING, playerController.currentPosition
            )
            mediaNotificationManager.notificationManager
                .notify(MediaNotificationManager.NOTIFICATION_ID, notification)
        }
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


    /**
     * Notifies of any state changes to the media currently playing
     */
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
            currentItem: PlayerQueueItem?
        ) {
            Log.e(TAG, "onCurrentItemChanged")
            updateNotificationForItemChanged(currentItem)
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