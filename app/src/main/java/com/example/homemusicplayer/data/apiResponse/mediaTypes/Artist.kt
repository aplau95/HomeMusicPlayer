package com.example.homemusicplayer.data.apiResponse.mediaTypes

import android.support.v4.media.MediaMetadataCompat
import com.example.homemusicplayer.data.apiResponse.mediaTypes.attributes.ArtistAttributes

class Artist : MediaType<ArtistAttributes>() {

    override fun toMediaMetadataCompat(): MediaMetadataCompat {
        val metadataBuilder = MediaMetadataCompat.Builder()
        metadataBuilder.putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, id)
        metadataBuilder.putString(MediaMetadataCompat.METADATA_KEY_ARTIST, attributes.name)
        metadataBuilder.putString(MediaMetadataCompat.METADATA_KEY_TITLE, attributes.name)
        metadataBuilder.putString(
            MediaMetadataCompat.METADATA_KEY_ART,
            attributes.artwork?.url
        )
        return metadataBuilder.build()
    }
}

