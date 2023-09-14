package com.example.homemusicplayer.media

import android.content.Context
import android.support.v4.media.MediaBrowserCompat
import android.util.Log
import androidx.media.MediaBrowserServiceCompat
import com.example.homemusicplayer.data.SearchRepository
import com.example.homemusicplayer.data.apiResponse.ApiResponse
import com.example.homemusicplayer.data.apiResponse.mediaTypes.MediaType
import com.example.homemusicplayer.data.apiResponse.mediaTypes.Song
import com.example.homemusicplayer.data.apiResponse.mediaTypes.attributes.TypeAttributes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.TreeMap


/**
 * Created by Assemble, Inc. on 2019-05-16.
 */
class MediaProvider(
    private val context: Context,
    private val searchRepository: SearchRepository
) {

    fun loadMediaItems(
        parentId: String,
        result: MediaBrowserServiceCompat.Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        searchQuery("harry", result)
    }

    private val scope = CoroutineScope(Dispatchers.IO)
    private val mainThread = CoroutineScope(Dispatchers.Main)

    private var searchResultsMetaData = TreeMap<String, MediaType<TypeAttributes>>()

    fun searchQuery(
        query: String,
        result: MediaBrowserServiceCompat.Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        result.detach()

        val mediaItems = mutableListOf<MediaBrowserCompat.MediaItem>()

        Log.e("MediaProvider", "searching for shit")
        scope.launch {
            searchRepository.getCatalogResources(query).collect { apiSearchResponse ->
                when (apiSearchResponse) {
                    is ApiResponse.Success -> {
                        apiSearchResponse.data.results?.topResults?.data?.map { mediaType ->
                            when (mediaType) {
                                is Song -> {
                                    val metadata = mediaType.toMediaMetadataCompat()
                                    val mediaItem = MediaBrowserCompat.MediaItem(
                                        metadata.description,
                                        MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
                                    )
                                    mediaItems.add(mediaItem)
                                }

                                else -> {}
                            }
                        }
                    }

                    else -> {}
                }

            }

            mainThread.launch {
                result.sendResult(mediaItems)
            }
        }

    }

//    private fun requestTopSongs(result: MediaBrowserServiceCompat.Result<List<MediaBrowserCompat.MediaItem>>) {
//
//
//        val url =
//            "https://api.music.apple.com/v1/catalog/us/charts?types=$MEDIA_TYPE_SONGS&genre=$GENRE_KIDS&limit=100&offset=0"
//        val request = object : StringRequest(
//            Method.GET, url,
//            Response.Listener { response ->
//                doAsync {
//                    val jsonObject = JsonParser().parse(response).asJsonObject
//                    val songsData =
//                        jsonObject.get("results").asJsonObject.get("songs").asJsonArray.get(0).asJsonObject
//                    val songs = songsData.get("data").asJsonArray ?: JsonArray()
//                    val mediaItems = ArrayList<MediaBrowserCompat.MediaItem>()
//                    topTracksMetadata = TreeMap()
//                    songs.forEach {
//                        val songJson = it as JsonObject
//                        val songPlaybackId = songJson.get("id").asString
//                        val songAttributes = songJson.get("attributes").asJsonObject
//                        val songName = songAttributes.get("name").asString
//                        val artistName = songAttributes.get("artistName").asString
//                        val albumName = songAttributes.get("albumName").asString
//                        val durationInMillis = songAttributes.get("durationInMillis").asLong
//                        val artwork = songAttributes.get("artwork").asJsonObject
//                        val imageSize = (context.resources.displayMetrics.density * 48).toInt()
//                        val artworkUrl =
//                            artwork.get("url").asString.replace("{w}", imageSize.toString())
//                                .replace("{h}", imageSize.toString())
//                        val fullImageSize = (context.resources.displayMetrics.density * 128).toInt()
//                        val fullArtworkUrl =
//                            artwork.get("url").asString.replace("{w}", fullImageSize.toString())
//                                .replace("{h}", fullImageSize.toString())
//                        val backgroundColor = artwork.get("bgColor").asString
//                        val textColor1 = artwork.get("textColor1").asString
//                        val textColor2 = artwork.get("textColor2").asString
//
//                        val song = Song(
//                            songPlaybackId,
//                            songName,
//                            artistName,
//                            albumName,
//                            artworkUrl,
//                            fullArtworkUrl,
//                            durationInMillis
//                        )
//                        song.backgroundColorHex = "#$backgroundColor"
//                        song.textColorHex = "#$textColor1"
//                        song.textColorAccentHex = "#$textColor2"
//                        topTracksMetadata[songPlaybackId] = song
//
//                        val metadata = song.toMediaMetadataCompat()
//                        val mediaItem = MediaBrowserCompat.MediaItem(
//                            metadata.description,
//                            MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
//                        )
//                        mediaItems.add(mediaItem)
//                    }
//
//                    uiThread { result.sendResult(mediaItems) }
//                }
//            },
//            Response.ErrorListener {
//                doAsync {
//                    val mediaItems = ArrayList<MediaBrowserCompat.MediaItem>()
//                    topTracksMetadata.forEach {
//                        val metadata = it.value.toMediaMetadataCompat()
//                        val mediaItem = MediaBrowserCompat.MediaItem(
//                            metadata.description,
//                            MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
//                        )
//                        mediaItems.add(mediaItem)
//                    }
//
//                    uiThread { result.sendResult(mediaItems) }
//                }
//            }) {
//            override fun getHeaders(): MutableMap<String, String> {
//                return authorizationHeaders
//            }
//        }
//        request.retryPolicy = DefaultRetryPolicy(
//            DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
//            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
//        )
//        request.setShouldCache(false)
//        requestQueue.add(request)
//    }
//
//    private fun requestPlaylists(result: MediaBrowserServiceCompat.Result<List<MediaBrowserCompat.MediaItem>>) {
//        val url = "https://api.music.apple.com/v1/me/library/playlists"
//        val request = object : StringRequest(Method.GET, url,
//            Response.Listener { response ->
//                doAsync {
//                    val jsonObject = JsonParser().parse(response).asJsonObject
//                    val data = jsonObject.get("data").asJsonArray
//                    val playlist = data.firstOrNull {
//                        val attributes = it.asJsonObject.get("attributes").asJsonObject
//                        attributes.get("name").asString == context.getString(R.string.playlist_name)
//                    }
//
//                    if (null != playlist) {
//                        val playlistId = (playlist as JsonObject).get("id").asString
//                        favoriteTracksPlaylistIdentifier = playlistId
//                        requestFavoriteSongs(result, playlistId)
//                    } else {
//                        createPlayList(result)
//                    }
//                }
//            },
//            Response.ErrorListener { error ->
//                Log.e(TAG, "$error")
//                val mediaItems = ArrayList<MediaBrowserCompat.MediaItem>()
//                result.sendResult(mediaItems)
//            }) {
//            override fun getHeaders(): MutableMap<String, String> {
//                return musicTokenHeaders
//            }
//        }
//        request.retryPolicy = DefaultRetryPolicy(
//            DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
//            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
//        )
//        request.setShouldCache(false)
//        requestQueue.add(request)
//    }
//
//    private fun requestFavoriteSongs(
//        result: MediaBrowserServiceCompat.Result<List<MediaBrowserCompat.MediaItem>>,
//        playlistId: String
//    ) {
//        val url = "https://api.music.apple.com/v1/me/library/playlists/$playlistId/tracks"
//        val request = object : StringRequest(Method.GET, url,
//            Response.Listener { response ->
//                doAsync {
//                    val jsonObject = JsonParser().parse(response).asJsonObject
//                    val tracks = jsonObject.get("data").asJsonArray
//                    val tracksIdentifiers = tracks.mapNotNull {
//                        val attributes = it.asJsonObject.get("attributes").asJsonObject
//                        val playParams =
//                            if (attributes.has("playParams")) attributes.get("playParams").asJsonObject else JsonObject()
//                        if (playParams.has("catalogId")) playParams.get("catalogId").asString else null
//                    }
//
//                    requestCatalogSongs(tracksIdentifiers, result)
//                }
//            },
//            Response.ErrorListener { error ->
//                Log.e(TAG, "$error")
//                val mediaItems = ArrayList<MediaBrowserCompat.MediaItem>()
//                result.sendResult(mediaItems)
//            }) {
//            override fun getHeaders(): MutableMap<String, String> {
//                return musicTokenHeaders
//            }
//        }
//        request.retryPolicy = DefaultRetryPolicy(
//            DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
//            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
//        )
//        request.setShouldCache(false)
//        requestQueue.add(request)
//    }
//
//    private fun requestCatalogSongs(
//        tracksIdentifiers: List<String>,
//        result: MediaBrowserServiceCompat.Result<List<MediaBrowserCompat.MediaItem>>
//    ) {
//        val url = "https://api.music.apple.com/v1/catalog/us/songs?ids=${
//            tracksIdentifiers.joinToString(separator = ",")
//        }&l=en-US"
//        val request = object : StringRequest(Method.GET, url,
//            Response.Listener { response ->
//                doAsync {
//                    val jsonObject = JsonParser().parse(response).asJsonObject
//                    val data = jsonObject.get("data").asJsonArray
//                    val songs = data.filter {
//                        val songAttributes = it.asJsonObject.get("attributes").asJsonObject
//                        songAttributes.get("genreNames").asJsonArray.contains(JsonPrimitive("Children's Music"))
//                    }
//                    val mediaItems = ArrayList<MediaBrowserCompat.MediaItem>()
//                    favoriteTracksMetadata = TreeMap()
//                    songs.forEach {
//                        val songJson = it as JsonObject
//                        val songPlaybackId = songJson.get("id").asString
//                        val songAttributes = songJson.get("attributes").asJsonObject
//                        val songName = songAttributes.get("name").asString
//                        val artistName = songAttributes.get("artistName").asString
//                        val albumName = songAttributes.get("albumName").asString
//                        val durationInMillis = songAttributes.get("durationInMillis").asLong
//                        val artwork = songAttributes.get("artwork").asJsonObject
//                        val imageSize = (context.resources.displayMetrics.density * 48).toInt()
//                        val artworkUrl =
//                            artwork.get("url").asString.replace("{w}", imageSize.toString())
//                                .replace("{h}", imageSize.toString())
//                        val fullImageSize = (context.resources.displayMetrics.density * 128).toInt()
//                        val fullArtworkUrl =
//                            artwork.get("url").asString.replace("{w}", fullImageSize.toString())
//                                .replace("{h}", fullImageSize.toString())
//                        val backgroundColor = artwork.get("bgColor").asString
//                        val textColor1 = artwork.get("textColor1").asString
//                        val textColor2 = artwork.get("textColor2").asString
//
//                        val song = Song(
//                            songPlaybackId,
//                            songName,
//                            artistName,
//                            albumName,
//                            artworkUrl,
//                            fullArtworkUrl,
//                            durationInMillis
//                        )
//                        song.backgroundColorHex = "#$backgroundColor"
//                        song.textColorHex = "#$textColor1"
//                        song.textColorAccentHex = "#$textColor2"
//                        favoriteTracksMetadata[songPlaybackId] = song
//
//                        val metadata = song.toMediaMetadataCompat()
//                        val mediaItem = MediaBrowserCompat.MediaItem(
//                            metadata.description,
//                            MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
//                        )
//                        mediaItems.add(mediaItem)
//                    }
//
//
//                    uiThread {
//                        result.sendResult(mediaItems)
//                    }
//                }
//            },
//            Response.ErrorListener { error ->
//                Log.e(TAG, "$error")
//                val mediaItems = ArrayList<MediaBrowserCompat.MediaItem>()
//                result.sendResult(mediaItems)
//            }) {
//            override fun getHeaders(): MutableMap<String, String> {
//                return musicTokenHeaders
//            }
//        }
//        request.retryPolicy = DefaultRetryPolicy(
//            DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
//            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
//        )
//        request.setShouldCache(false)
//        requestQueue.add(request)
//    }
//
//    private fun createPlayList(result: MediaBrowserServiceCompat.Result<List<MediaBrowserCompat.MediaItem>>) {
//        val url = "https://api.music.apple.com/v1/me/library/playlists"
//        val attributes = HashMap<String, String>()
//        attributes["name"] = context.getString(R.string.playlist_name)
//        attributes["description"] = context.getString(R.string.playlist_name)
//        val body = HashMap<String, HashMap<String, String>>()
//        body["attributes"] = attributes
//
//        val request = object : JsonObjectRequest(Method.POST, url, JSONObject(body),
//            Response.Listener { response ->
//                doAsync {
//                    val jsonObject = JsonParser().parse(response.toString()).asJsonObject
//                    val playlist = jsonObject.get("data").asJsonArray.first()
//                    val playlistId = (playlist as JsonObject).get("id").asString
//                    requestFavoriteSongs(result, playlistId)
//                }
//            },
//            Response.ErrorListener { error ->
//                Log.e(TAG, "$error")
//                val mediaItems = ArrayList<MediaBrowserCompat.MediaItem>()
//                result.sendResult(mediaItems)
//            }) {
//            override fun getHeaders(): MutableMap<String, String> {
//                return musicTokenHeaders
//            }
//        }
//
//        request.retryPolicy = DefaultRetryPolicy(
//            DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
//            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
//        )
//        request.setShouldCache(false)
//        requestQueue.add(request)
//    }
//
//    fun getSong(mediaId: String): Song {
//        return topTracksMetadata[mediaId] ?: favoriteTracksMetadata[mediaId]
//    }
//
//    fun getTrackMetadata(mediaId: String): MediaMetadataCompat {
//        val song = getSong(mediaId)
//        return song.toMediaMetadataCompat()
//    }
//
//    companion object {
//
//        private val TAG = MediaProvider::class.java.simpleName
//        private val CANONICAL_NAME = MediaProvider::class.java.canonicalName
//        private const val GENRE_KIDS = 4
//        private const val MEDIA_TYPE_SONGS = "songs"
//        val ROOT_ID = "$CANONICAL_NAME.root"
//        val TOP_SONGS_ROOT_ID = "$CANONICAL_NAME.top_songs_root"
//        val FAVORITES_ROOT_ID = "$CANONICAL_NAME.favorite_songs_root"
//        val EXTRA_MEDIA_REQUEST_ERROR = "$CANONICAL_NAME.media_request_error"
//    }
}
