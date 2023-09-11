package com.example.homemusicplayer.data.apiResponse.search.searchResponse

import com.example.homemusicplayer.data.apiResponse.types.MediaType
import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @field:SerializedName("results") val results: Results,
)

data class Results(
    @field:SerializedName("topResults") val topResults: TopResultsSearchResult,
)

data class TopResultsSearchResult(
    @field:SerializedName("data") val data: List<MediaType<*>>,
)

