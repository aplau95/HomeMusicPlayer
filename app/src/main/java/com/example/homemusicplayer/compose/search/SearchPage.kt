package com.example.homemusicplayer.compose.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.homemusicplayer.data.apiResponse.ApiResponse
import com.example.homemusicplayer.data.apiResponse.search.searchResponse.SearchResponse
import com.example.homemusicplayer.data.apiResponse.search.searchSuggestionsResponse.SearchSuggestionResponse
import com.example.homemusicplayer.data.apiResponse.types.Album
import com.example.homemusicplayer.data.apiResponse.types.Artist
import com.example.homemusicplayer.data.apiResponse.types.MediaType
import com.example.homemusicplayer.data.apiResponse.types.Song
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
            .padding(20.dp)

    ) {
        SearchBar(searchTerm, onSearch)
        when (terms) {
            is ApiResponse.Success -> {
                LazyColumn {
                    items(count = terms.data.results.termSuggestion.size) { index ->
                        SearchSuggestionRow(term = terms.data.results.termSuggestion[index].searchTerm)

                    }
                }


            }

            else -> {

            }
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



@Composable
fun MediaTypeItem(
    resource: MediaType<*>
) {

    val color = androidx.compose.material3.MaterialTheme.colorScheme.primaryContainer
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                val strokeWidth = density * .7f
                val y = size.height - strokeWidth / 2

                drawLine(
                    color,
                    Offset(0f, y),
                    Offset(size.width, y),
                    strokeWidth
                )
            }
    ) {

        when (resource) {
            is Album -> AlbumItem(resource)
            is Artist -> ArtistItem(resource)
            is Song -> SongItem(resource)
        }
    }
}


@Composable
fun ArtistItem(
    resource: Artist
) {
    AsyncImage(
        modifier = Modifier
            .size(50.dp)
            .clip(CircleShape),
        model = resource.attributes?.artwork?.url
            ?.replace("{w}", "100", false)
            ?.replace("{h}", "100", false),
        contentDescription = "Test"
    )
    Column {
        Text(resource.attributes!!.name)
        Text("Artist")
    }

}

@Composable
fun RowScope.AlbumItem(
    resource: Album
) {

    AsyncImage(
        modifier = Modifier
            .size(50.dp)
            .clip(RoundedCornerShape(10)),
        model = resource.attributes?.artwork?.url
            ?.replace("{w}", "100", false)
            ?.replace("{h}", "100", false),
        contentDescription = "Test"
    )
    Column {
        Text(resource.attributes!!.name)
        Text("Album - ${resource.attributes!!.artistName}")
    }
}

@Composable
fun RowScope.SongItem(
    resource: Song
) {
    AsyncImage(
        modifier = Modifier
            .size(50.dp)
            .clip(RoundedCornerShape(10)),
        model = resource.attributes?.artwork?.url
            ?.replace("{w}", "100", false)
            ?.replace("{h}", "100", false),
        contentDescription = "Test"
    )
    Column {
        Text(resource.attributes!!.name)
        Text("Song - ${resource.attributes!!.artistName}")
    }
}

