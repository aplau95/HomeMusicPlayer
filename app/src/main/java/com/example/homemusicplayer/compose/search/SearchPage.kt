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

val testItems = listOf("Hi", "Hello", "Whee")

@Composable
fun SearchPage(
    modifier: Modifier,
    viewModel: SearchViewModel,
) {
//    val terms by viewModel.terms.collectAsState()
//    val catalogs by viewModel.catalog.collectAsState()
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





