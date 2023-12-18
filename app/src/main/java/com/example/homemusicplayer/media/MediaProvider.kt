package com.example.homemusicplayer.media

import android.content.Context
import android.support.v4.media.MediaBrowserCompat
import androidx.media.MediaBrowserServiceCompat
import com.example.homemusicplayer.data.SearchRepository
import com.example.homemusicplayer.data.apiResponse.ApiResponse
import com.example.homemusicplayer.data.apiResponse.mediaTypes.MediaType
import com.example.homemusicplayer.data.apiResponse.mediaTypes.Song
import com.example.homemusicplayer.data.apiResponse.mediaTypes.attributes.TypeAttributes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.TreeMap


/**
 * Created by Assemble, Inc. on 2019-05-16.
 */
class MediaProvider(
    private val context: Context,
    private val searchRepository: SearchRepository
) {

    fun loadMediaItems(
        parentId: String,
        result: MediaBrowserServiceCompat.Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        searchQuery("harry", result)
    }

    private val scope = CoroutineScope(Dispatchers.IO)
    private val mainThread = CoroutineScope(Dispatchers.Main)

    private var searchResultsMetaData = TreeMap<String, MediaType<TypeAttributes>>()

    fun searchQuery(
        query: String,
        result: MediaBrowserServiceCompat.Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        result.detach()

        val mediaItems = mutableListOf<MediaBrowserCompat.MediaItem>()

        scope.launch {
            searchRepository.getCatalogResources(query).collect { apiSearchResponse ->
                when (apiSearchResponse) {
                    is ApiResponse.Success -> {
                        apiSearchResponse.data.results?.topResults?.data?.map { mediaType ->
                            when (mediaType) {
                                is Song -> {
                                    val metadata = mediaType.toMediaMetadataCompat()
                                    val mediaItem = MediaBrowserCompat.MediaItem(
                                        metadata.description,
                                        MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
                                    )
                                    mediaItems.add(mediaItem)
                                }

                                else -> {}
                            }
                        }
                    }

                    else -> {}
                }

            }

            mainThread.launch {
                result.sendResult(mediaItems)
            }
        }

    }
}
