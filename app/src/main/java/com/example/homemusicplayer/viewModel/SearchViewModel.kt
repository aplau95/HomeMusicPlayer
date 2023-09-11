package com.example.homemusicplayer.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.homemusicplayer.api.SearchService
import com.example.homemusicplayer.data.SearchRepository
import com.example.homemusicplayer.data.apiResponse.ApiResponse
import com.example.homemusicplayer.data.apiResponse.search.searchResponse.SearchResponse
import com.example.homemusicplayer.data.apiResponse.search.searchSuggestionsResponse.SearchSuggestionResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    val service: SearchService,
    val repository: SearchRepository
) : BaseViewModel() {


    var _terms = MutableStateFlow<ApiResponse<SearchSuggestionResponse>?>(null)
    val terms: StateFlow<ApiResponse<SearchSuggestionResponse>?> = _terms

    var _catalog = MutableStateFlow<ApiResponse<SearchResponse>?>(null)
    val catalog: StateFlow<ApiResponse<SearchResponse>?> = _catalog

    var _searchTerm = MutableStateFlow("")
    val searchTerm = _searchTerm

    fun getCatalogResources(query: String) = baseRequest(
        _catalog,
        coroutinesErrorHandler
    ) {
        repository.getCatalogResources(term = query)
    }

    fun getSearchSuggestions(query: String) = baseRequest(
        _terms,
        coroutinesErrorHandler
    ) {
        repository.getSearchTermResources(query)
    }

    init {
        viewModelScope.launch {
            searchTerm.debounce(1000).collect { query ->
                if (query.isNotEmpty()) {
                    catalog.value
                    getCatalogResources(query)
                    getSearchSuggestions(query)
                }
            }
        }
    }
}