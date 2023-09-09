package com.example.homemusicplayer.compose.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.homemusicplayer.viewModel.SearchViewModel

val testItems = listOf("Hi", "Hello", "Whee")

@Composable
fun SearchPage(
    viewModel: SearchViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {

    val scope = rememberCoroutineScope()

    val terms = viewModel.terms.collectAsState()

    Column(
        modifier = modifier
            .background(MaterialTheme.colors.background)
            .padding(20.dp)

    ) {
        SearchBar()
        terms.value?.let {
            LazyColumn {
                items(count = it.termSuggestion.size) { index ->
                    Text(text = it.termSuggestion[index].searchTerm)
                }


            }
        }

    }
}

@Preview
@Composable
fun SearchPagePreview(
    modifier: Modifier = Modifier
) {
//    SearchPage(
//        modifier.fillMaxSize()
//    )
}