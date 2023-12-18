package com.example.homemusicplayer.compose.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.homemusicplayer.data.apiResponse.ApiResponse
import com.example.homemusicplayer.data.apiResponse.mediaTypes.MediaType
import com.example.homemusicplayer.viewModel.SearchViewModel

/**
 * The search page wrapper. We use this to destructure references that are within the
 * SearchViewModel and its functions
 */
@Composable
fun SearchPage(
    modifier: Modifier,
    viewModel: SearchViewModel,
) {
    val searchPageState by viewModel.searchPageState.collectAsState()

    val searchTerm by viewModel.searchTerm.collectAsState()

    SearchPage(
        modifier,
        searchPageState,
        viewModel::updateSearchTerms,
        viewModel::playMedia,
        searchTerm
    )
}

/**
 * Overloaded function renders the state delineated by the SearchViewModel.
 */
@Composable
fun SearchPage(
    modifier: Modifier,
    searchPageState: ApiResponse<SearchViewModel.SearchPageState>,
    onSearch: (String) -> Unit,
    playMedia: (MediaType<*>) -> Unit,
    searchTerm: String
) {

    Column(
        modifier = modifier
            .background(MaterialTheme.colors.background)
            .padding(horizontal = 20.dp)
    ) {
        SearchBar(searchTerm, onSearch)
        when (searchPageState) {
            is ApiResponse.Success -> {

                val catalog = searchPageState.data.searchCatalog.results?.topResults?.data
                val suggestions = searchPageState.data.termSuggestions.results.termSuggestion
                LazyColumn {

                    // Takes the list of suggestions that originate from Apple Musics search suggestion
                    // API response and renders them
                    items(
                        count = suggestions.size,
                        key = {
                            suggestions[it].searchTerm
                        }
                    ) { index ->
                        SearchSuggestionRow(
                            term = suggestions[index].searchTerm,
                            searchTerm,
                            onSearch
                        )
                    }

                    // Takes the list of catalog items that originate from Apple Musics catalog
                    // API response and renders them
                    catalog?.let { mediaList ->
                        items(
                            count = mediaList.size,
                            key = { mediaList[it].id!! }
                        ) { index ->
                            MediaTypeItem(mediaList[index], playMedia)
                        }
                    }

                }
            }

            else -> {}
        }
    }
}





