package com.example.homemusicplayer.data.apiResponse.mediaTypes

import android.support.v4.media.MediaMetadataCompat
import com.example.homemusicplayer.data.apiResponse.mediaTypes.attributes.ArtistAttributes

class Artist : MediaType<ArtistAttributes>() {

    /**
     * Required function by the extending class to create a MediaMetadataCompat instance that is
     * needed to pass to Apple's Media Player API to actually play the context
     */
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

