//package com.example.homemusicplayer.media
//
//import android.content.ComponentName
//import android.content.Context
//import android.support.v4.media.MediaBrowserCompat
//
///**
// * Copyright (C) 2017 Apple, Inc. All rights reserved.
// */
//
//class MediaBrowserHelper(
//    private val context: Context,
//    private val listener: Listener
//) : MediaBrowserCompat.ConnectionCallback() {
//
//    private val mediaBrowser: MediaBrowserCompat = MediaBrowserCompat(
//        context,
//        ComponentName(context, MediaPlaybackService::class.java),
//        this,
//        null
//    )
//
//
//    fun connect() {
//        mediaBrowser.connect()
//    }
//
//    fun disconnect() {
//        mediaBrowser.disconnect()
//    }
//
//    override fun onConnected() {
//        listener.onMediaBrowserConnected(mediaBrowser)
//    }
//
//    override fun onConnectionSuspended() {}
//
//
//    override fun onConnectionFailed() {
//    }
//
//
//    interface Listener {
//
//        fun onMediaBrowserConnected(mediaBrowser: MediaBrowserCompat)
//
//    }
//
//}
