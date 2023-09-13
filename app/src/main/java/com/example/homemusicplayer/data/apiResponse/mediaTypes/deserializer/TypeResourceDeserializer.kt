package com.example.homemusicplayer.data.apiResponse.mediaTypes.deserializer

import com.example.homemusicplayer.data.apiResponse.mediaTypes.Album
import com.example.homemusicplayer.data.apiResponse.mediaTypes.Artist
import com.example.homemusicplayer.data.apiResponse.mediaTypes.MediaType
import com.example.homemusicplayer.data.apiResponse.mediaTypes.Playlist
import com.example.homemusicplayer.data.apiResponse.mediaTypes.Song
import com.example.homemusicplayer.data.apiResponse.mediaTypes.attributes.TypeAttributes
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class TypeResourceDeserializer : JsonDeserializer<MediaType<TypeAttributes>?> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext
    ): MediaType<TypeAttributes>? {
        return when (json?.asJsonObject?.get("type")?.asString) {
            "albums" -> context.deserialize(json, Album::class.java)
            "songs" -> context.deserialize(json, Song::class.java)
            "artists" -> context.deserialize(json, Artist::class.java)
            "playlists" -> context.deserialize(json, Playlist::class.java)
            else -> null
        }
    }
}