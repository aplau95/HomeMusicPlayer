package com.example.homemusicplayer.data.apiResponse.mediaTypes

import android.support.v4.media.MediaMetadataCompat
import com.example.homemusicplayer.data.apiResponse.mediaTypes.attributes.TypeAttributes

abstract class MediaType<T : TypeAttributes>(
    val type: String? = null,
    val id: String? = null,
    val href: String? = null,
) {

    lateinit var attributes: T

    abstract fun toMediaMetadataCompat(): MediaMetadataCompat
}