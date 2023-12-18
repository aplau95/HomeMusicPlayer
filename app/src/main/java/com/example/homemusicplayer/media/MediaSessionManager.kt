package com.example.homemusicplayer.media

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.ResultReceiver
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.RatingCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import com.apple.android.music.playback.controller.MediaPlayerController
import com.apple.android.music.playback.model.ErrorConditionException
import com.apple.android.music.playback.model.MediaContainerType
import com.apple.android.music.playback.model.MediaItemType
import com.apple.android.music.playback.model.MediaPlayerException
import com.apple.android.music.playback.model.PlaybackRepeatMode
import com.apple.android.music.playback.model.PlaybackShuffleMode
import com.apple.android.music.playback.model.PlaybackState
import com.apple.android.music.playback.model.PlayerQueueItem
import com.apple.android.music.playback.queue.CatalogPlaybackQueueItemProvider
import com.example.homemusicplayer.media.MediaExtensions.CONTAINER_TYPE
import com.example.homemusicplayer.media.MediaExtensions.ITEM_TYPE
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Class that is created by the PlaybackSessionService that holds the actual MediaPlayerController
 * used to play content. We implement MediaSessionCompat.Callback() to receive playback info such as
 * play song/album/playlist etc. Refer to SearchViewModel playMedia() for example. The playback info
 * is relayed downstream to here.
 */
internal class MediaSessionManager(
    private val playerController: MediaPlayerController,
    mediaSession: MediaSessionCompat
) :
    MediaSessionCompat.Callback(),
    MediaPlayerController.Listener {

    private val mediaSession: MediaSessionCompat
    private val metadataBuilder: MediaMetadataCompat.Builder
    private var metadata: MediaMetadataCompat? = null
    private val playbackStateBuilder: PlaybackStateCompat.Builder
    private val artworkWidth: Int
    private val artworkHeight: Int
    private var queueItems: ArrayList<MediaSessionCompat.QueueItem> = ArrayList()

    init {
        playerController.addListener(this)
        this.mediaSession = mediaSession
        metadataBuilder = MediaMetadataCompat.Builder()
        playbackStateBuilder = PlaybackStateCompat.Builder()
        artworkWidth = 280
        artworkHeight = 280
    }

    override fun onCommand(command: String, extras: Bundle?, cb: ResultReceiver) {
        when (command) {
            COMMAND_SWAP_QUEUE -> {
                cb.send(RESULT_ADD_QUEUE_ITEMS, null)
            }
        }

    }

    fun addListener(listener: MediaPlayerController.Listener) {
        playerController.addListener(listener)
    }

    override fun onMediaButtonEvent(mediaButtonEvent: Intent): Boolean {
        return false
    }

    override fun onPrepare() {
        mediaSession.isActive = true
    }

    override fun onPrepareFromMediaId(mediaId: String, extras: Bundle) {}
    override fun onPrepareFromSearch(query: String, extras: Bundle) {}
    override fun onPrepareFromUri(uri: Uri, extras: Bundle) {}
    override fun onPlay() {
        Log.e(TAG, "onPlay")
        playerController.play()
    }

    override fun onPlayFromMediaId(mediaId: String, extras: Bundle?) {


        val builder = CatalogPlaybackQueueItemProvider.Builder()
        var containerType = MediaContainerType.NONE
        var itemType = MediaItemType.UNKNOWN
        if (extras != null) {
            containerType = extras.getLong(CONTAINER_TYPE, MediaContainerType.NONE.toLong()).toInt()
            itemType = extras.getLong(ITEM_TYPE, MediaItemType.UNKNOWN.toLong()).toInt()
        }
        if (containerType != MediaContainerType.NONE) {
            Log.e("MediaSessionManager", "${containerType}")
            builder.containers(containerType, mediaId)
        } else {
            Log.e("MediaSessionManager", "${itemType}")
            builder.items(itemType, mediaId)
        }
        playerController.prepare(builder.build(), true)
    }

    override fun onPlayFromSearch(query: String, extras: Bundle) {}
    override fun onPlayFromUri(uri: Uri, extras: Bundle) {}
    override fun onSkipToQueueItem(id: Long) {
        playerController.skipToQueueItemWithId(id)
    }

    override fun onPause() {
        playerController.pause()
    }

    override fun onSkipToNext() {
        playerController.skipToNextItem()
    }

    override fun onSkipToPrevious() {
        playerController.skipToPreviousItem()
    }

    override fun onFastForward() {}
    override fun onRewind() {}
    override fun onStop() {
        playerController.stop()
    }

    override fun onSeekTo(pos: Long) {
        playerController.seekToPosition(pos)
    }

    override fun onSetRating(rating: RatingCompat) {}
    override fun onSetRepeatMode(repeatMode: Int) {}
    override fun onSetShuffleMode(shuffleMode: Int) {}
    override fun onCustomAction(action: String, extras: Bundle) {}
    override fun onAddQueueItem(description: MediaDescriptionCompat) {
        description.let {
            queueItems.add(
                MediaSessionCompat.QueueItem(
                    description,
                    description.hashCode().toLong()
                )
            )
            Log.e(TAG, "onAddQueueItem ${description.title}")
            mediaSession.setQueue(queueItems.subList(0, 0))
        }
    }

    override fun onAddQueueItem(description: MediaDescriptionCompat, index: Int) {
        Log.e(TAG, "onRemoveQueueItem()")
    }

    override fun onRemoveQueueItem(description: MediaDescriptionCompat) {
        Log.e(TAG, "onRemoveQueueItem()")
    }

    override fun onPlayerStateRestored(playerController: MediaPlayerController) {
        Log.e(TAG, "onPlayerStateRestored()")
    }

    override fun onPlaybackStateChanged(
        playerController: MediaPlayerController,
        previousState: Int,
        currentState: Int
    ) {
        Log.e(
            TAG,
            "onPlaybackStateChanged() prevState: $previousState currentState: $currentState"
        )
    }

    override fun onPlaybackStateUpdated(playerController: MediaPlayerController) {
        Log.e(TAG, "onPlaybackStateUpdated()")
    }

    override fun onBufferingStateChanged(
        playerController: MediaPlayerController,
        buffering: Boolean
    ) {
        Log.e(
            TAG,
            "onBufferingStateChanged() buffering: $buffering"
        )
    }

    override fun onCurrentItemChanged(
        playerController: MediaPlayerController,
        previousItem: PlayerQueueItem?,
        currentItem: PlayerQueueItem?
    ) {
        Log.e(
            TAG,
            "onCurrentItemChanged() prevItemQueueId: " + (previousItem?.playbackQueueId
                ?: -1)
                    + " currItemQueueId: " + (currentItem?.playbackQueueId ?: -1)
        )

//        updateMetaData(previousItem, currentItem)
//        updatePlaybackState(playerController.playbackState, playerController.isBuffering)
    }

    override fun onItemEnded(
        playerController: MediaPlayerController,
        queueItem: PlayerQueueItem,
        endPosition: Long
    ) {
        Log.e(
            TAG,
            "onItemEnded() queueItem: " + queueItem.playbackQueueId + " endPosition: " + endPosition
        )
    }

    override fun onMetadataUpdated(
        playerController: MediaPlayerController,
        currentItem: PlayerQueueItem
    ) {
        Log.e(TAG, "onMetadataUpdated() item: " + currentItem.playbackQueueId)
    }

    override fun onPlaybackQueueChanged(
        playerController: MediaPlayerController,
        playbackQueueItems: List<PlayerQueueItem>
    ) {
        Log.e(TAG, "onPlaybackQueueChanged() numOfItems: " + playbackQueueItems.size)
    }

    override fun onPlaybackQueueItemsAdded(
        playerController: MediaPlayerController,
        queueInsertionType: Int,
        containerType: Int,
        itemType: Int
    ) {
        Log.e(
            TAG,
            "onPlaybackQueueItemsAdded() insertionType: $queueInsertionType containerType: $containerType itemType: $itemType"
        )
    }

    override fun onPlaybackError(
        playerController: MediaPlayerController,
        error: MediaPlayerException
    ) {
        Log.e(TAG, "onPlaybackError()")
        val t = error.cause
        if (t is ErrorConditionException) {
            Log.e(TAG, "onPlaybackError() errorCode: " + t.errorCode)
        }
    }

    override fun onPlaybackRepeatModeChanged(
        playerController: MediaPlayerController,
        @PlaybackRepeatMode currentRepeatMode: Int
    ) {
        Log.e(TAG, "onPlaybackRepeatModeChanged()")
        mediaSession.setRepeatMode(convertRepeatMode(currentRepeatMode))
    }

    override fun onPlaybackShuffleModeChanged(
        playerController: MediaPlayerController,
        @PlaybackShuffleMode currentShuffleMode: Int
    ) {
        Log.e(TAG, "onPlaybackShuffleModeChanged()")
        mediaSession.setShuffleMode(convertShuffleMode(currentShuffleMode))
    }


    companion object {


        private val CANONICAL_NAME = MediaSessionManager::class.java.canonicalName
        const val FIVE_SECONDS_IN_MILLIS = 5 * 1000

        val COMMAND_SWAP_QUEUE = "$CANONICAL_NAME.command_swap_queue"
        val COMMAND_GET_TRACK = "$CANONICAL_NAME.command_get_track"
        val COMMAND_GET_CURRENT_TRACK = "$CANONICAL_NAME.command_current_track"
        val COMMAND_GET_CURRENT_TRACK_ELAPSED_TIME =
            "$CANONICAL_NAME.command_current_track_elapsed_time"
        val COMMAND_GET_FAVORITES_PLAYLIST_ID = "$CANONICAL_NAME.command_favorites_playlist_id"
        val COMMAND_STOP = "$CANONICAL_NAME.command_stop"

        val EXTRA_QUEUE_IDENTIFIER = "$CANONICAL_NAME.extra_queue_identifier"
        val EXTRA_CURRENT_TRACK_ELAPSED_TIME = "$CANONICAL_NAME.extra_current_track_elapsed_time"
        val EXTRA_CURRENT_TRACK = "$CANONICAL_NAME.extra_current_track"
        val EXTRA_TRACK_ID = "$CANONICAL_NAME.extra_track_id"
        val EXTRA_CURRENT_PLAYLIST_ID = "$CANONICAL_NAME.extra_current_playlist_id"
        val EXTRA_TRACK = "$CANONICAL_NAME.extra_track"
        val EXTRA_FAVORITES_PLAYLIST_ID = "$CANONICAL_NAME.extra_track"

        const val RESULT_ERROR = 0
        const val RESULT_ADD_QUEUE_ITEMS = 1
        const val RESULT_CURRENT_TRACK_ELAPSED_TIME = 2
        const val RESULT_CURRENT_TRACK = 3
        const val RESULT_TRACK = 4
        const val RESULT_FAVORITES_PLAYLIST_ID = 5
        const val RESULT_OK = 6


        private val TAG = "MediaSessionManager"
        private val RELEASE_DATE_FORMAT: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        private val MESSAGE_INIT = 1
        private val MESSAGE_LOAD_ARTWORK = 2
        private val MESSAGE_UPDATE_ARTWORK = 3
        private fun convertPlaybackState(
            @PlaybackState playbackState: Int,
            buffering: Boolean
        ): Int {
            when (playbackState) {
                PlaybackState.STOPPED -> return PlaybackStateCompat.STATE_STOPPED
                PlaybackState.PAUSED -> return PlaybackStateCompat.STATE_PAUSED
                PlaybackState.PLAYING -> return if (buffering) PlaybackStateCompat.STATE_BUFFERING else PlaybackStateCompat.STATE_PLAYING
                else -> return PlaybackStateCompat.STATE_NONE
            }
        }

        private fun convertRepeatMode(@PlaybackRepeatMode repeatMode: Int): Int {
            when (repeatMode) {
                PlaybackRepeatMode.REPEAT_MODE_ALL -> return PlaybackStateCompat.REPEAT_MODE_ALL
                PlaybackRepeatMode.REPEAT_MODE_ONE -> return PlaybackStateCompat.REPEAT_MODE_ONE
                PlaybackRepeatMode.REPEAT_MODE_OFF -> return PlaybackStateCompat.REPEAT_MODE_NONE
                else -> return PlaybackStateCompat.REPEAT_MODE_NONE
            }
        }

        private fun convertShuffleMode(@PlaybackShuffleMode shuffleMode: Int): Int {
            when (shuffleMode) {
                PlaybackShuffleMode.SHUFFLE_MODE_OFF -> return PlaybackStateCompat.SHUFFLE_MODE_NONE
                PlaybackShuffleMode.SHUFFLE_MODE_SONGS -> return PlaybackStateCompat.SHUFFLE_MODE_ALL
            }
            return PlaybackStateCompat.SHUFFLE_MODE_NONE
        }

        private fun allowedActions(playerController: MediaPlayerController): Long {
            // TODO: This will need to take into account queue state, etc as to whether skip is allowed
            var result =
                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS or PlaybackStateCompat.ACTION_SKIP_TO_NEXT
            when (playerController.playbackState) {
                PlaybackState.PLAYING -> result = result or PlaybackStateCompat.ACTION_PAUSE
                PlaybackState.PAUSED -> result =
                    result or (PlaybackStateCompat.ACTION_PLAY or PlaybackStateCompat.ACTION_STOP)

                PlaybackState.STOPPED -> result = result or PlaybackStateCompat.ACTION_PLAY
            }
            return result
        }

        private fun formatReleaseDate(releaseDate: Date?): String? {
            return if (releaseDate == null) {
                null
            } else RELEASE_DATE_FORMAT.format(releaseDate)
        }
    }
}