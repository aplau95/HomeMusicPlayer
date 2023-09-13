package com.example.homemusicplayer.api

import com.example.homemusicplayer.data.apiResponse.search.searchResponse.SearchResponse
import com.example.homemusicplayer.data.apiResponse.search.searchSuggestionsResponse.SearchSuggestionResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface SearchService {

    @GET("catalog/us/search/suggestions")
    suspend fun searchTermSuggestions(
        // (Required) The suggestion kinds to include in the results. Possible Values: terms, topResults
        @Query("kinds") query: List<String> = listOf("terms"),

        // Locale used for search
        @Query("l") localization: String = "en",

        // Default 5 max is 10
        @Query("limit") limit: Int = 5,

        // (Required) The text input to use for search suggestions.
        @Query("term", encoded = true) term: String,

        // Possible Values: activities, albums, apple-curators, artists, curators, music-videos, playlists, record-labels, songs, stations
        @Query("types") types: List<String>? = null
    ): Response<SearchSuggestionResponse>

    @GET("catalog/us/search")
    suspend fun searchCatalogResources(
        // The entered text for the search with ‘+’ characters between each word, to replace spaces
        // (for example term=james+br).
        @Query("term", encoded = true) term: String,

        // The localization to use, specified by a language tag. The possible values are in the
        // supportedLanguageTags array belonging to the Storefront object specified by storefront.
        // Otherwise, the default is defaultLanguageTag in Storefront.
        @Query("l") localization: String = "en", // Locale used for search

        // The number of objects or number of objects in the specified relationship returned.
        // Default: 5
        // Maximum Value: 25
        @Query("limit") limit: Int = 10,

        // The next page or group of objects to fetch.
        @Query("offset") offset: String? = null,

        // Required) The list of the types of resources to include in the results.
        // Possible Values: activities, albums, apple-curators, artists, curators, music-videos,
        // playlists, record-labels, songs, stations
        @Query("types") types: List<String> = emptyList(),

        // A list of modifications to apply to the request.
        // Value: topResults
        @Query("with") with: String = "topResults"
    ): Response<SearchResponse>

}