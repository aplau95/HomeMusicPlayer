package com.example.homemusicplayer.viewModel

import android.os.Bundle
import android.os.ResultReceiver
import android.support.v4.media.MediaBrowserCompat
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.apple.android.music.sdk.testapp.service.MediaSessionManager
import com.example.homemusicplayer.data.SearchRepository
import com.example.homemusicplayer.data.apiResponse.ApiResponse
import com.example.homemusicplayer.data.apiResponse.mediaTypes.MediaType
import com.example.homemusicplayer.data.apiResponse.search.searchResponse.SearchResponse
import com.example.homemusicplayer.data.apiResponse.search.searchSuggestionsResponse.SearchSuggestionResponse
import com.example.homemusicplayer.media.MediaPlayerServiceConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    val repository: SearchRepository,
    val serviceConnection: MediaPlayerServiceConnection
) : BaseViewModel() {


    var _terms = MutableStateFlow<ApiResponse<SearchSuggestionResponse>>(ApiResponse.Loading)
    val terms = _terms.asStateFlow()


    var _catalog = MutableStateFlow<ApiResponse<SearchResponse>>(ApiResponse.Loading)
    val catalog = _catalog.asStateFlow()

    var _searchTerm = MutableStateFlow("")
    val searchTerm = _searchTerm.asStateFlow()

    data class SearchPageState(
        val searchCatalog: SearchResponse,
        val termSuggestions: SearchSuggestionResponse
    )

    var _searchPageState = MutableStateFlow<ApiResponse<SearchPageState>>(ApiResponse.Loading)
    val searchPageState = _searchPageState.asStateFlow()

    fun playMedia(mediaType: MediaType<*>) {
        Log.e("SearchViewModel", "id is ${mediaType.id}")
        val songMetadata = mediaType.toMediaMetadataCompat()
        serviceConnection.mediaControllerCompat.let { mc ->
            mc.sendCommand(
                MediaSessionManager.COMMAND_SWAP_QUEUE,
                null,
                object : ResultReceiver(null) {
                    override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
                        if (resultCode == MediaSessionManager.RESULT_ADD_QUEUE_ITEMS) {
//                        for (mediaItem in songs) {

                            mc.addQueueItem(songMetadata.description)
//                        }

                            mc.transportControls?.prepare()

                            serviceConnection.transportControl.playFromMediaId(
                                songMetadata.description.mediaId,
                                null
                            )
                        }


                    }
                })
        }


    }

    fun getSearchResults(term: String) = baseRequest(
        _searchPageState,
        coroutinesErrorHandler
    ) {
        val termUpdated = term.replace("\\s".toRegex(), "+")
        repository.getCatalogResources(termUpdated)
            .combine(repository.getSearchTermResources(termUpdated)) { catalog, terms ->
                return@combine when {
                    catalog is ApiResponse.Success && terms is ApiResponse.Success ->
                        ApiResponse.Success(
                            data = SearchPageState(
                                searchCatalog = catalog.data,
                                termSuggestions = terms.data
                            )
                        )

                    catalog is ApiResponse.Loading -> ApiResponse.Loading
                    catalog is ApiResponse.Failure -> ApiResponse.Failure<SearchPageState>(
                        errorMessage = catalog.errorMessage,
                        code = catalog.code
                    )

                    else -> throw Exception("No")
                }
            }
    }

    fun updateSearchTerms(text: String) {
        _searchTerm.value = text
        serviceConnection.searchQuery(text)
    }

    inner class MediaBrowserSubscription : MediaBrowserCompat.SubscriptionCallback() {

        override fun onChildrenLoaded(
            parentId: String,
            children: MutableList<MediaBrowserCompat.MediaItem>
        ) {
            super.onChildrenLoaded(parentId, children)
        }
    }


    init {
        viewModelScope.launch {
            searchTerm.collect { query ->
                getSearchResults(query)
            }
        }

        _searchTerm.value = "jmsn"
    }
}