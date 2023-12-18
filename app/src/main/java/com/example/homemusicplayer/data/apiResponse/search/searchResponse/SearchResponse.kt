package com.example.homemusicplayer.data.apiResponse.search.searchResponse

import com.example.homemusicplayer.data.apiResponse.mediaTypes.MediaType
import com.google.gson.annotations.SerializedName

// Destructuring JSON response
data class SearchResponse(
    @field:SerializedName("results") val results: Results?,
)

// Destructuring JSON response
data class Results(
    @field:SerializedName("topResults") val topResults: TopResultsSearchResult,
)

// Destructuring JSON response and generating into MediaType
data class TopResultsSearchResult(
    @field:SerializedName("data") val data: List<MediaType<*>>,
)

