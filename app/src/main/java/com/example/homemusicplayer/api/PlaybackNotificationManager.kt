//package com.example.homemusicplayer.api
//
//import android.app.Notification
//import android.app.NotificationChannel
//import android.app.NotificationManager
//import android.app.PendingIntent
//import android.content.Context
//import android.content.Intent
//import android.os.Build
//import android.os.Handler
//import android.os.RemoteException
//import android.support.v4.media.MediaMetadataCompat
//import android.support.v4.media.session.MediaControllerCompat
//import android.support.v4.media.session.PlaybackStateCompat
//import android.view.KeyEvent
//import androidx.core.app.NotificationManagerCompat
//import androidx.media.app.NotificationCompat
//import com.example.homemusicplayer.R
//import com.example.homemusicplayer.media.MediaPlaybackService
//import java.util.BitSet
//
//internal class PlaybackNotificationManager(
//    private val service: MediaPlaybackService,
//    private val backgroundHandler: Handler
//) :
//    MediaControllerCompat.Callback() {
//
//    private val notificationManager: NotificationManagerCompat
//    private var mediaController: MediaControllerCompat? = null
//    private var postedNotification = false
//
//    init {
//        notificationManager = NotificationManagerCompat.from(service)
//        notificationManager.cancel(NOTIFICATION_ID)
//        createNotificationChannel()
//        createMediaController()
//    }
//
//    override fun onPlaybackStateChanged(state: PlaybackStateCompat) {
//        if (postedNotification) {
//            updateNotification()
//        }
//    }
//
//    override fun onMetadataChanged(metadata: MediaMetadataCompat) {
//        if (postedNotification) {
//            updateNotification()
//        }
//    }
//
//    override fun onSessionDestroyed() {
//        mediaController!!.unregisterCallback(this)
//        mediaController = null
//    }
//
//    fun start() {
//        service.startService(Intent(service, MediaPlaybackService::class.java))
//        if (mediaController == null) {
//            createMediaController()
//        }
//        val notification = createNotification()
//        if (notification != null) {
//            service.startForeground(NOTIFICATION_ID, notification)
//            postedNotification = true
//        }
//    }
//
//    fun stop(removeNotification: Boolean) {
//        service.stopForeground(removeNotification)
//        if (removeNotification) {
//            if (mediaController != null) {
//                mediaController!!.unregisterCallback(this)
//                mediaController = null
//            }
//            postedNotification = false
//        }
//    }
//
//    private fun createMediaController() {
//        val sessionToken = service.sessionToken
//        if (sessionToken != null) {
//            try {
//                mediaController = MediaControllerCompat(service, service.sessionToken!!)
//                mediaController!!.registerCallback(this, backgroundHandler)
//            } catch (e: RemoteException) {
//            }
//        }
//    }
//
//    private fun createNotificationChannel() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val notificationManager =
//                service.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            val name = service.getString(R.string.action_settings)
//            val channel = NotificationChannel(
//                NOTIFICATION_CHANNEL_ID,
//                name,
//                NotificationManager.IMPORTANCE_LOW
//            )
//            channel.enableVibration(false)
//            channel.enableLights(false)
//            notificationManager.createNotificationChannel(channel)
//        }
//    }
//
//    private fun createNotification(): Notification? {
//        val playbackState = mediaController!!.playbackState ?: return null
//        val state = playbackState.state
//        if (state == PlaybackStateCompat.STATE_STOPPED || state == PlaybackStateCompat.STATE_NONE) {
//            return null
//        }
//        val notificationStyle = NotificationCompat.MediaStyle()
//        notificationStyle.setMediaSession(service.sessionToken)
//        val notificationBuilder = androidx.core.app.NotificationCompat.Builder(
//            service, NOTIFICATION_CHANNEL_ID
//        )
//        notificationBuilder.setStyle(notificationStyle)
//        notificationBuilder.setSmallIcon(R.drawable.ic_launcher_foreground)
//        notificationBuilder.setOngoing(isPlaying(state))
//        notificationBuilder.setVisibility(androidx.core.app.NotificationCompat.VISIBILITY_PUBLIC)
//        notificationBuilder.setContentIntent(mediaController!!.sessionActivity)
//        if (state == PlaybackStateCompat.STATE_PLAYING && playbackState.position != PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN) {
//            notificationBuilder.setWhen(System.currentTimeMillis() - playbackState.position)
//            notificationBuilder.setShowWhen(true)
//            notificationBuilder.setUsesChronometer(true)
//        } else {
//            notificationBuilder.setWhen(0)
//            notificationBuilder.setShowWhen(false)
//            notificationBuilder.setUsesChronometer(false)
//        }
//        val mediaMetadata = mediaController!!.metadata
//        if (mediaMetadata != null) {
//            val mediaDescription = mediaMetadata.description
//            notificationBuilder.setContentTitle(mediaDescription.title)
//            notificationBuilder.setContentText(mediaDescription.subtitle)
//            notificationBuilder.setSubText(mediaDescription.description)
//            notificationBuilder.setLargeIcon(mediaDescription.iconBitmap)
//        }
//        val allowedActions = playbackState.actions
//        var actionCount = 0
//        val compactActions = BitSet(5)
//        if (isAllowedAction(allowedActions, PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS)) {
//            notificationBuilder.addAction(
//                R.drawable.ic_menu_camera,
//                service.getString(R.string.search),
//                createActionIntent(
//                    service, KeyEvent.KEYCODE_MEDIA_PREVIOUS
//                )
//            )
//            compactActions.set(actionCount)
//            ++actionCount
//        }
//        if (isAllowedAction(allowedActions, PlaybackStateCompat.ACTION_PLAY)) {
//            notificationBuilder.addAction(
//                R.drawable.side_nav_bar,
//                service.getString(R.string.library),
//                createActionIntent(
//                    service, KeyEvent.KEYCODE_MEDIA_PLAY
//                )
//            )
//            compactActions.set(actionCount)
//            ++actionCount
//        } else if (isAllowedAction(allowedActions, PlaybackStateCompat.ACTION_PAUSE)) {
//            notificationBuilder.addAction(
//                R.drawable.ic_menu_slideshow,
//                service.getString(R.string.search),
//                createActionIntent(
//                    service, KeyEvent.KEYCODE_MEDIA_PAUSE
//                )
//            )
//            compactActions.set(actionCount)
//            ++actionCount
//        }
//        if (isAllowedAction(allowedActions, PlaybackStateCompat.ACTION_SKIP_TO_NEXT)) {
//            notificationBuilder.addAction(
//                R.drawable.ic_menu_slideshow,
//                service.getString(R.string.menu_home),
//                createActionIntent(
//                    service, KeyEvent.KEYCODE_MEDIA_NEXT
//                )
//            )
//            compactActions.set(actionCount)
//            ++actionCount
//        }
//        if (isAllowedAction(allowedActions, PlaybackStateCompat.ACTION_STOP)) {
//            val stopIntent = createActionIntent(
//                service, KeyEvent.KEYCODE_MEDIA_STOP
//            )
//            notificationBuilder.setDeleteIntent(stopIntent)
//            notificationStyle.setShowCancelButton(true)
//            notificationStyle.setCancelButtonIntent(stopIntent)
//        }
//        if (compactActions.cardinality() > 0) {
//            notificationStyle.setShowActionsInCompactView(*compactActionsList(compactActions))
//        }
//        return notificationBuilder.build()
//    }
//
//    private fun updateNotification() {
//        val notification = createNotification()
//        if (notification != null) {
//            notificationManager.notify(NOTIFICATION_ID, notification)
//        }
//    }
//
//    companion object {
//
//        private const val NOTIFICATION_ID = 0xA123
//        private const val NOTIFICATION_CHANNEL_ID = "playback"
//        private fun createActionIntent(context: Context, mediaKeyCode: Int): PendingIntent {
//            val intent = Intent(context, MediaPlaybackService::class.java)
//            intent.action = Intent.ACTION_MEDIA_BUTTON
//            intent.putExtra(Intent.EXTRA_KEY_EVENT, KeyEvent(KeyEvent.ACTION_DOWN, mediaKeyCode))
//            return PendingIntent.getService(context, mediaKeyCode, intent, 0)
//        }
//
//        private fun isAllowedAction(allowedActions: Long, action: Long): Boolean {
//            return action and allowedActions == action
//        }
//
//        private fun isPlaying(state: Int): Boolean {
//            return state == PlaybackStateCompat.STATE_PLAYING || state == PlaybackStateCompat.STATE_BUFFERING || state == PlaybackStateCompat.STATE_CONNECTING
//        }
//
//        private fun compactActionsList(actionIndices: BitSet): IntArray {
//            val result = IntArray(actionIndices.cardinality())
//            val count = actionIndices.size()
//            var i = 0
//            var j = 0
//            while (i < count) {
//                if (actionIndices[i]) {
//                    result[j++] = i
//                }
//                i++
//            }
//            return result
//        }
//    }
//}