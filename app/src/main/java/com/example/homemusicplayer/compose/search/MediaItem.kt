package com.example.homemusicplayer.compose.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.homemusicplayer.data.apiResponse.mediaTypes.Album
import com.example.homemusicplayer.data.apiResponse.mediaTypes.Artist
import com.example.homemusicplayer.data.apiResponse.mediaTypes.Artwork
import com.example.homemusicplayer.data.apiResponse.mediaTypes.MediaType
import com.example.homemusicplayer.data.apiResponse.mediaTypes.Playlist
import com.example.homemusicplayer.data.apiResponse.mediaTypes.Song
import com.example.homemusicplayer.data.apiResponse.mediaTypes.attributes.ArtistAttributes

/**
 * Rendered MediaItem. This can be an Album, Artist, Song
 *
 * @param resource, the media we want to render. This can be an Album, Artist, Song
 * @param playMedia callback that is triggered on click to play that media item
 */
@Composable
fun MediaTypeItem(
    resource: MediaType<*>,
    playMedia: (MediaType<*>) -> Unit,
) {

    val color = androidx.compose.material3.MaterialTheme.colorScheme.primaryContainer

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clickable {
                // Tell the callback that lives in the
                playMedia(resource)
            }
            .drawBehind {
                val strokeWidth = density * .7f
                val y = size.height - strokeWidth / 2

                drawLine(
                    color,
                    Offset(0f, y),
                    Offset(size.width, y),
                    strokeWidth
                )
            },
        verticalAlignment = Alignment.CenterVertically
    ) {

        AsyncImage(
            modifier = Modifier
                .size(60.dp)
                .createMediaItemShape(resource),
            model = resource.attributes.artwork?.url
                ?.replace("{w}", "100", false)
                ?.replace("{h}", "100", false),
            contentDescription = "Test"
        )
        Column(modifier = Modifier.padding(start = 12.dp)) {
            Text(resource.attributes.name!!)
            when (resource) {
                is Artist -> MediaItemDescription("Artist")
                is Album -> MediaItemDescription("Album • ${resource.attributes.artistName}")
                is Song -> MediaItemDescription("Song • ${resource.attributes.artistName}")
                is Playlist -> MediaItemDescription("Playlist • ${resource.attributes.curatorName}")
            }
        }
    }
}

// The description in the MediaItem
@Composable
fun MediaItemDescription(desc: String) {
    Text(
        text = desc,
        color = Color.Gray
    )
}

// Creates the proper shape based off media item
fun Modifier.createMediaItemShape(
    mediaType: MediaType<*>,
): Modifier {
    return when (mediaType) {
        is Artist -> this.clip(CircleShape)
        else -> this.clip(RoundedCornerShape(10))
    }
}

// Test view
@Preview
@Composable
fun MediaItem() {
    val artist = Artist().apply {
        attributes = ArtistAttributes(

        ).apply {
            name = "JSMN"
            artwork = Artwork(
                url = "https://is1-ssl.mzstatic.com/image/thumb/Music125/v4/66/d5/9a/66d59a9f-c2d4-8da2-7053-ea2e97f0b7f7/21UMGIM38174.rgb.jpg/{w}x{h}bb.jpg"
            )
        }
    }

    MediaTypeItem(resource = artist, {})
}