package com.example.homemusicplayer.data.apiResponse.search.searchSuggestionsResponse.suggestion

import com.google.gson.annotations.SerializedName

// Destructuring JSON response
data class Suggestion(
    @field:SerializedName("suggestions") val termSuggestion: List<TermSuggestion>,
)
