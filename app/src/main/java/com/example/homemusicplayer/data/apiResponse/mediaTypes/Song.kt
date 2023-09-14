package com.example.homemusicplayer.data.apiResponse.mediaTypes

import android.support.v4.media.MediaMetadataCompat
import com.apple.android.music.playback.model.MediaItemType
import com.example.homemusicplayer.data.apiResponse.mediaTypes.attributes.SongAttributes
import com.example.homemusicplayer.media.MediaExtensions

class Song : MediaType<SongAttributes>() {

    var backgroundColorHex: String = ""
    var textColorHex: String = ""
    var textColorAccentHex: String = ""

//    constructor(parcel: Parcel) : this(
//        parcel.readString() ?: "",
//        parcel.readString() ?: "",
//        parcel.readString() ?: "",
//        parcel.readString() ?: "",
//        parcel.readString() ?: "",
//        parcel.readString() ?: "",
//        parcel.readLong()
//    ) {
//        backgroundColorHex = parcel.readString() ?: ""
//        textColorHex = parcel.readString() ?: ""
//        textColorAccentHex = parcel.readString() ?: ""
//    }

    override fun toMediaMetadataCompat(): MediaMetadataCompat {
        val metadataBuilder = MediaMetadataCompat.Builder()
        metadataBuilder.putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, id)
        metadataBuilder.putString(MediaMetadataCompat.METADATA_KEY_ALBUM, attributes.albumName)
        metadataBuilder.putString(MediaMetadataCompat.METADATA_KEY_ARTIST, attributes.artistName)
        metadataBuilder.putString(MediaMetadataCompat.METADATA_KEY_TITLE, attributes.name)
        metadataBuilder.putLong(
            MediaMetadataCompat.METADATA_KEY_DURATION,
            attributes.durationInMillis
        )
        metadataBuilder.putString(
            MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI,
            attributes.artwork?.url
        )
        metadataBuilder.putLong(
            MediaExtensions.ITEM_TYPE,
            MediaItemType.SONG.toLong()
        )
//        metadataBuilder.putString(MediaMetadataCompat.METADATA_KEY_ART_URI, fullArtworkUrl)
        return metadataBuilder.build()
    }

//    override fun writeToParcel(parcel: Parcel, flags: Int) {
//        parcel.writeString(id)
//        parcel.writeString(name)
//        parcel.writeString(artistName)
//        parcel.writeString(albumName)
//        parcel.writeString(artworkUrl)
//        parcel.writeString(fullArtworkUrl)
//        parcel.writeLong(durationInMillis)
//        parcel.writeString(backgroundColorHex)
//        parcel.writeString(textColorHex)
//        parcel.writeString(textColorAccentHex)
//    }
//
//    override fun describeContents(): Int {
//        return 0
//    }

//    companion object CREATOR : Parcelable.Creator<Song> {
//
////        override fun createFromParcel(parcel: Parcel): Song {
////            return Song(parcel)
////        }
//
//        override fun newArray(size: Int): Array<Song?> {
//            return arrayOfNulls(size)
//        }
//
////        @JvmStatic
////        fun newInstance(mediaMetadataCompat: MediaMetadataCompat): Song {
////            val songId = mediaMetadataCompat.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID)
////            val songName = mediaMetadataCompat.getString(MediaMetadataCompat.METADATA_KEY_TITLE)
////            val artistName = mediaMetadataCompat.getString(MediaMetadataCompat.METADATA_KEY_ARTIST)
////            val albumName = mediaMetadataCompat.getString(MediaMetadataCompat.METADATA_KEY_ALBUM)
////            val artworkUrl =
////                mediaMetadataCompat.getString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI)
////            val fullArtworkUrl =
////                mediaMetadataCompat.getString(MediaMetadataCompat.METADATA_KEY_ART_URI)
////            val durationInMillis =
////                mediaMetadataCompat.getLong(MediaMetadataCompat.METADATA_KEY_DURATION)
////            return Song(
////                songId,
////                songName,
////                artistName,
////                albumName,
////                artworkUrl,
////                fullArtworkUrl,
////                durationInMillis
////            )
////        }
//    }
}

