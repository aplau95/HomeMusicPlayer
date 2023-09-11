package com.example.homemusicplayer.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.homemusicplayer.R
import dagger.hilt.android.AndroidEntryPoint
import utils.SingleLiveEvent
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.atomic.AtomicBoolean

@AndroidEntryPoint
class TcpServerService : Service() {

    private val binder = TcpServerServiceBinder()

    private val mGenerator = java.util.Random()

    val randomNumber: Int
        get() = mGenerator.nextInt(100)

    inner class TcpServerServiceBinder : Binder() {

        fun getService(): TcpServerService = this@TcpServerService
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    private var _trigger = SingleLiveEvent<Boolean>().apply { value = false }
    val trigger = _trigger
    private var serverSocket: ServerSocket? = null
    private val working = AtomicBoolean(true)
    private val runnable = Runnable {
        var socket: Socket? = null
        try {
            serverSocket = ServerSocket(PORT)
            while (working.get()) {
                if (serverSocket != null) {
                    socket = serverSocket!!.accept()
                    Log.i(TAG, "New client: $socket")
                    val dataInputStream = DataInputStream(socket.getInputStream())
                    val dataOutputStream = DataOutputStream(socket.getOutputStream())

                    // Use threads for each client to communicate with them simultaneously
                    val t: Thread =
                        TcpClientHandler(dataInputStream, dataOutputStream, ::receiveCallback)
                    t.start()
                } else {
                    Log.e(TAG, "Couldn't create ServerSocket!")
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            try {
                socket?.close()
            } catch (ex: IOException) {
                ex.printStackTrace()
            }
        }
    }

    private fun receiveCallback() {
        _trigger.postValue(true)
    }

    override fun onCreate() {
        startMeForeground()
        Log.i(TAG, "Start")
        Thread(runnable).start()
    }

    override fun onDestroy() {
        Log.i(TAG, "destroy")
        working.set(false)
    }

    private fun startMeForeground() {
        val NOTIFICATION_CHANNEL_ID = packageName
        val channelName = "Tcp Server Background Service"
        val chan = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            channelName,
            NotificationManager.IMPORTANCE_NONE
        )
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val manager = (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
        manager.createNotificationChannel(chan)
        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
        val notification = notificationBuilder.setOngoing(true)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Tcp Server is running in background")
            .setPriority(NotificationManager.IMPORTANCE_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
        startForeground(2, notification)
    }

    companion object {

        private val TAG = TcpServerService::class.java.simpleName
        private const val PORT = 9876
    }
}