package com.example.homemusicplayer.data.apiResponse.types

class Album : MediaType<AlbumAttributes>()

class AlbumAttributes(
    val artistName: String,
    val name: String,
    val trackCount: Int
) : TypeAttributes()
