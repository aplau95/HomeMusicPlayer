package com.example.homemusicplayer.data.apiResponse.types

class Song : MediaType<SongAttributes>()

class SongAttributes(
    val albumName: String,
    val artistName: String,
    val durationInMillis: Int,
    val name: String
) : TypeAttributes()

