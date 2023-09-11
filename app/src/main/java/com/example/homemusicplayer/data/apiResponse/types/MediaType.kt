package com.example.homemusicplayer.data.apiResponse.types

open class MediaType<T : TypeAttributes>(
    val type: String? = null,
    val id: String? = null,
    val href: String? = null,
    var attributes: T? = null
)