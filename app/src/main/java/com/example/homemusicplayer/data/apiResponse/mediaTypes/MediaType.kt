package com.example.homemusicplayer.data.apiResponse.mediaTypes

import com.example.homemusicplayer.data.apiResponse.mediaTypes.attributes.TypeAttributes

open class MediaType<T : TypeAttributes>(
    val type: String? = null,
    val id: String? = null,
    val href: String? = null,
) {

    lateinit var attributes: T
}