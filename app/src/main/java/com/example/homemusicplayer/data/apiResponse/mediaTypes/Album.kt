package com.example.homemusicplayer.data.apiResponse.mediaTypes

import android.support.v4.media.MediaMetadataCompat
import com.apple.android.music.playback.model.MediaContainerType
import com.example.homemusicplayer.data.apiResponse.mediaTypes.attributes.AlbumAttributes
import com.example.homemusicplayer.media.MediaExtensions

class Album : MediaType<AlbumAttributes>() {

    /**
     * Required function by the extending class to create a MediaMetadataCompat instance that is
     * needed to pass to Apple's Media Player API to actually play the context
     */
    override fun toMediaMetadataCompat(): MediaMetadataCompat {
        val metadataBuilder = MediaMetadataCompat.Builder()
        metadataBuilder.putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, id)
        metadataBuilder.putString(MediaMetadataCompat.METADATA_KEY_ALBUM, attributes.name)
        metadataBuilder.putString(MediaMetadataCompat.METADATA_KEY_ARTIST, attributes.artistName)
        metadataBuilder.putString(MediaMetadataCompat.METADATA_KEY_TITLE, attributes.name)
        metadataBuilder.putString(
            MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI,
            attributes.artwork?.url
        )
        metadataBuilder.putLong(
            MediaExtensions.CONTAINER_TYPE,
            MediaContainerType.ALBUM.toLong()
        )
        return metadataBuilder.build()
    }
}

