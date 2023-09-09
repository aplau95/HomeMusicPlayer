package com.example.homemusicplayer.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homemusicplayer.api.SearchService
import com.example.homemusicplayer.data.apiResponse.search.searchSuggestionsResponse.suggestion.Suggestion
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    val service: SearchService
) : ViewModel() {

    private var queryString: String? = savedStateHandle["plantName"]

    var _terms = MutableStateFlow<Suggestion?>(null)
    val terms: StateFlow<Suggestion?> = _terms

    var _searchTerm = MutableStateFlow("")
    val searchTerm = _searchTerm

    init {
        viewModelScope.launch {
            searchTerm.debounce(1000).collect { query ->
                if (query.isNotEmpty()) {
                    _terms.value =
                        service.searchTermSuggestions(listOf("terms"), term = query).results
                }
            }
        }
    }
}