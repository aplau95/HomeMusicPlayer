package com.example.homemusicplayer.compose.search

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.homemusicplayer.viewModel.SearchViewModel

@Composable
fun SearchBar(
    viewModel: SearchViewModel = hiltViewModel()
) {

    val textSearch = viewModel.searchTerm.collectAsState()

    TextField(
        value = textSearch.value,
        onValueChange = {
            viewModel._searchTerm.value = it
        },
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
    )
}