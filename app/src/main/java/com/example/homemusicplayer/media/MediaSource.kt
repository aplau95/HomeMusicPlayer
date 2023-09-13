package com.example.homemusicplayer.media

import com.example.homemusicplayer.data.SearchRepository
import javax.inject.Inject

class MediaSource @Inject constructor(
    private val repository: SearchRepository
)