package com.example.homemusicplayer.api

import com.example.homemusicplayer.data.apiResponse.search.searchSuggestionsResponse.SearchSuggestionResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface SearchService {

    @Headers(
        "Authorization: Bearer eyJhbGciOiJFUzI1NiIsImtpZCI6IjhQRjgyNlVCVk4iLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiI0TjNTVFY3NTVXIiwiZXhwIjoxNjk0MjM4NjA3LCJpYXQiOjE2OTQxOTU0MDd9.p4jFDwaUjjvTDK4AZV1b5gtm_7ByW9v-EtqXeP2izyAbCDsOvanInHkfdmBBAtS5uRMzlLlbsywtCFdNECTAUg"
    )
    @GET("us/search/suggestions")
    suspend fun searchTermSuggestions(
        @Query("kinds") query: List<String>, // (Required) The suggestion kinds to include in the results. Possible Values: terms, topResults
        @Query("l") localization: String = "en", // Locale used for search
        @Query("limit") limit: Int = 5, // Default 5 max is 10
        @Query("term") term: String, // (Required) The text input to use for search suggestions.
        @Query("types") types: List<String>? = null// Possible Values: activities, albums, apple-curators, artists, curators, music-videos, playlists, record-labels, songs, stations
    ): SearchSuggestionResponse

    companion object {

        private const val BASE_URL = "https://api.music.apple.com/v1/catalog/"

        fun create(): SearchService {
            val logger =
                HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(SearchService::class.java)
        }
    }
}