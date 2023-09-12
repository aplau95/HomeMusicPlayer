package com.example.homemusicplayer.compose.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SearchSuggestionRow(term: String) {

    val color = androidx.compose.material3.MaterialTheme.colorScheme.primaryContainer
    Row(
        modifier = Modifier
            .height(40.dp)
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
        ,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Icon(Icons.Rounded.Search, contentDescription = "Search")
        Text(
            text = term,
            modifier = Modifier.padding(start = 4.dp)
        )

    }
}

@Preview
@Composable
fun PreviewSearchSuggestion() {
    Box(modifier = Modifier.background(Color.White)) {
        SearchSuggestionRow(term = "Test")
    }

}