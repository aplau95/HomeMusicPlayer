package com.example.homemusicplayer.data.apiResponse.search.searchSuggestionsResponse

import com.example.homemusicplayer.data.apiResponse.search.searchSuggestionsResponse.suggestion.Suggestion
import com.google.gson.annotations.SerializedName

data class SearchSuggestionResponse(
    @field:SerializedName("results") val results: Suggestion,
)