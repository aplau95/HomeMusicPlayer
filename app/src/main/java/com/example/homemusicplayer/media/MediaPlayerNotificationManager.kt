package com.example.homemusicplayer.media

import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import androidx.media3.common.Player
import androidx.media3.ui.PlayerNotificationManager
import com.example.homemusicplayer.R
import com.example.homemusicplayer.media.K.PLAYBACK_NOTIFICATION_CHANNEL_ID

@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
class MediaPlayerNotificationManager(
    context: Context,
    sessionToken: MediaSessionCompat.Token,
    notificationListener: PlayerNotificationManager.NotificationListener
) {

    val notificationManager: PlayerNotificationManager

    init {
        val mediaController = MediaControllerCompat(context, sessionToken)

        val builder = PlayerNotificationManager.Builder(
            context,
            K.PLAYBACK_NOTIFICATION_ID,
            PLAYBACK_NOTIFICATION_CHANNEL_ID
        )

        with(builder) {
            setMediaDescriptionAdapter(DescriptionAdapter(mediaController))
            setNotificationListener(notificationListener)
            setChannelDescriptionResourceId(R.string.nofication_channel_id)
            setChannelDescriptionResourceId(R.string.nofication_channel_description)
        }

        notificationManager = builder.build()

        with(notificationManager) {
            setMediaSessionToken(sessionToken)
            setSmallIcon(R.drawable.ic_menu_camera)
            setUseRewindAction(false)
            setUseFastForwardAction(false)
        }
    }

    fun hideNotification() {
        notificationManager.setPlayer(null)
    }

    fun showNotification(player: Player) {
        notificationManager.setPlayer(player)
    }


    inner class DescriptionAdapter(private val controller: MediaControllerCompat) :
        PlayerNotificationManager.MediaDescriptionAdapter {

        override fun getCurrentContentTitle(player: Player): CharSequence {
            return controller.metadata.description.title.toString()
        }

        override fun createCurrentContentIntent(player: Player): PendingIntent? {
            return controller.sessionActivity
        }

        override fun getCurrentContentText(player: Player): CharSequence? {
            return controller.metadata.description.subtitle
        }

        override fun getCurrentLargeIcon(
            player: Player,
            callback: PlayerNotificationManager.BitmapCallback
        ): Bitmap? {
            return null
        }

    }
}