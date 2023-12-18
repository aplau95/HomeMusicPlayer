package com.example.homemusicplayer.data.apiResponse.mediaTypes

import android.support.v4.media.MediaMetadataCompat
import com.example.homemusicplayer.data.apiResponse.mediaTypes.attributes.TypeAttributes

/**
 * Base class for defining the structure the JSON response
 *
 * TypeAttributes is additional info that is unique to the specific media type. e.g. a song and its
 * duration is unique to its type
 */
abstract class MediaType<T : TypeAttributes>(
    val type: String? = null,
    val id: String? = null,
    val href: String? = null,
) {

    lateinit var attributes: T

    /**
     * Required function by the extending class to create a MediaMetadataCompat instance that is
     * needed to pass to Apple's Media Player API to actually play the context
     */
    abstract fun toMediaMetadataCompat(): MediaMetadataCompat
}