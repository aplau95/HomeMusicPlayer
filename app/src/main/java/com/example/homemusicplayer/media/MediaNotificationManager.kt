package com.example.homemusicplayer.media

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.media.session.MediaButtonReceiver
import com.apple.android.music.playback.model.PlayerMediaItem
import com.example.homemusicplayer.MainActivity
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch

/**
 * Handles the creation and rendering of the notification that show info such as artist/song playing
 *
 * Playback control to be implemented
 */
class MediaNotificationManager(private val playbackSessionService: PlaybackSessionService) {

    var notificationManager: NotificationManager
        private set
    private var playAction: NotificationCompat.Action
    private var pauseAction: NotificationCompat.Action
    private var previousAction: NotificationCompat.Action
    private val nextAction: NotificationCompat.Action


    init {
        notificationManager =
            playbackSessionService.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()

        playAction = NotificationCompat.Action(
            androidx.media3.ui.R.drawable.exo_notification_play,
            "Playing",
            MediaButtonReceiver.buildMediaButtonPendingIntent(
                playbackSessionService,
                PlaybackStateCompat.ACTION_PLAY
            )
        )

        pauseAction = NotificationCompat.Action(
            androidx.media3.ui.R.drawable.exo_notification_pause,
            "Pause",
            MediaButtonReceiver.buildMediaButtonPendingIntent(
                playbackSessionService,
                PlaybackStateCompat.ACTION_PAUSE
            )
        )

        previousAction = NotificationCompat.Action(
            androidx.media3.ui.R.drawable.exo_ic_skip_previous,
            "Previous",
            MediaButtonReceiver.buildMediaButtonPendingIntent(
                playbackSessionService,
                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
            )
        )

        nextAction = NotificationCompat.Action(
            androidx.media3.ui.R.drawable.exo_ic_skip_next,
            "Next",
            MediaButtonReceiver.buildMediaButtonPendingIntent(
                playbackSessionService,
                PlaybackStateCompat.ACTION_SKIP_TO_NEXT
            )
        )
    }

    fun getNotification(
        mediaItem: PlayerMediaItem,
        token: MediaSessionCompat.Token,
        state: Int,
        currentPosition: Long
    ): Notification {
        val builder = buildNotification(mediaItem, token, state, currentPosition)
        return builder.build()
    }

    private fun buildNotification(
        mediaItem: PlayerMediaItem,
        token: MediaSessionCompat.Token,
        state: Int,
        currentPosition: Long
    ): NotificationCompat.Builder {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) createChannel()

        val isPlaying =
            (state == PlaybackStateCompat.STATE_PLAYING) or (state == PlaybackStateCompat.STATE_BUFFERING)

        val builder = NotificationCompat.Builder(playbackSessionService, CHANNEL_ID)
        builder
            .setColor(
                ContextCompat.getColor(
                    playbackSessionService,
                    androidx.media3.ui.R.color.exo_white
                )
            )
            .setSmallIcon(androidx.media3.ui.R.drawable.exo_edit_mode_logo)
            .setContentIntent(createContentIntent())
            .setContentTitle(mediaItem.title)
            .setContentText(mediaItem.albumTitle)
            .setLargeIcon(null)
            .setDeleteIntent(
                MediaButtonReceiver.buildMediaButtonPendingIntent(
                    playbackSessionService, PlaybackStateCompat.ACTION_STOP
                )
            )
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

        if (state == PlaybackStateCompat.STATE_PLAYING && currentPosition != PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN) {
            builder.setWhen(System.currentTimeMillis() - currentPosition)
            builder.setShowWhen(true)
            builder.setUsesChronometer(true)
        } else {
            builder.setWhen(0)
            builder.setShowWhen(false)
            builder.setUsesChronometer(false)
        }

        builder.addAction(previousAction)

        builder.addAction(if (isPlaying) pauseAction else playAction)

        builder.addAction(nextAction)


        playbackSessionService.backgroundThread.launch {
            val bitmap = Picasso.get().load(mediaItem.artworkUrl).get()
            playbackSessionService.mainThread.launch {
                builder.setLargeIcon(bitmap)
                    .setStyle(
                        NotificationCompat.BigPictureStyle()
                            .bigPicture(bitmap)

                    )
                notificationManager.notify(NOTIFICATION_ID, builder.build())
            }
        }
        return builder
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        if (null == notificationManager.getNotificationChannel(CHANNEL_ID)) {
            val name = "MediaSession"
            val description = "MediaSession and MediaPlayer"
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = description
            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createContentIntent(): PendingIntent {
        val openUI = Intent(playbackSessionService, MainActivity::class.java)
        openUI.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        return PendingIntent.getActivity(
            playbackSessionService,
            REQUEST_CODE,
            openUI,
            PendingIntent.FLAG_CANCEL_CURRENT
        )
    }

    // endregion


    companion object {

        private val TAG = MediaNotificationManager::class.java.simpleName
        private val CANONICAL_NAME = MediaNotificationManager::class.java.simpleName
        private val CHANNEL_ID = "$CANONICAL_NAME.channel"
        private const val REQUEST_CODE = 501
        const val NOTIFICATION_ID = 416
    }
}
