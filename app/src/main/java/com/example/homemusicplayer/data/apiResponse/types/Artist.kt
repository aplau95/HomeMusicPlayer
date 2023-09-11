package com.example.homemusicplayer.data.apiResponse.types

class Artist : MediaType<ArtistAttributes>()

class ArtistAttributes(
    val name: String,
    val url: String? = null
) : TypeAttributes()