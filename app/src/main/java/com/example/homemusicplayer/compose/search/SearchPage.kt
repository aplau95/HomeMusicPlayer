package com.example.homemusicplayer.compose.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.homemusicplayer.data.apiResponse.ApiResponse
import com.example.homemusicplayer.data.apiResponse.search.searchResponse.SearchResponse
import com.example.homemusicplayer.data.apiResponse.search.searchSuggestionsResponse.SearchSuggestionResponse
import com.example.homemusicplayer.viewModel.SearchViewModel

val testItems = listOf("Hi", "Hello", "Whee")

@Composable
fun SearchPage(
    modifier: Modifier,
    viewModel: SearchViewModel = hiltViewModel(),
) {
    val terms = viewModel.terms.collectAsState().value
    val catalogs = viewModel.catalog.collectAsState().value
    val searchTerm = viewModel.searchTerm.collectAsState().value

    SearchPage(
        modifier,
        terms,
        catalogs,
        viewModel::updateSearchTerms,
        searchTerm
    )
}

@Composable
fun SearchPage(
    modifier: Modifier,
    terms: ApiResponse<SearchSuggestionResponse>,
    catalogs: ApiResponse<SearchResponse>,
    onSearch: (String) -> Unit,
    searchTerm: String
) {

    Column(
        modifier = modifier
            .background(MaterialTheme.colors.background)
            .padding(horizontal = 20.dp)
    ) {
        SearchBar(searchTerm, onSearch)
        when (terms) {
            is ApiResponse.Success -> {
                LazyColumn {
                    items(count = terms.data.results.termSuggestion.size) { index ->
                        SearchSuggestionRow(
                            term = terms.data.results.termSuggestion[index].searchTerm,
                            searchTerm,
                            onSearch
                        )

                    }
                }


            }

            else -> {}
        }
        when (catalogs) {
            is ApiResponse.Success -> {
                LazyColumn {
                    items(count = catalogs.data.results.topResults.data.size) { index ->
                        MediaTypeItem(catalogs.data.results.topResults.data[index])
                    }
                }
            }

            else -> {}
        }
    }
}





