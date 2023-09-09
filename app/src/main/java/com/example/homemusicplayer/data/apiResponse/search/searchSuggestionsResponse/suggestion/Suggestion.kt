package com.example.homemusicplayer.data.apiResponse.search.searchSuggestionsResponse.suggestion

import com.google.gson.annotations.SerializedName

data class Suggestion(
    @field:SerializedName("suggestions") val termSuggestion: List<TermSuggestion>,
)
